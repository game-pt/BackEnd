package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.*;
import com.a405.gamept.game.dto.response.FightResultGetResponseDto;
import com.a405.gamept.game.dto.response.MonsterGetResponseDto;
import com.a405.gamept.game.entity.*;
import com.a405.gamept.game.repository.*;
import com.a405.gamept.game.util.GameData;
import com.a405.gamept.game.util.enums.GameErrorMessage;
import com.a405.gamept.game.util.exception.GameException;
import com.a405.gamept.play.entity.Game;
import com.a405.gamept.play.entity.FightingEnemy;
import com.a405.gamept.play.entity.Player;
import com.a405.gamept.play.repository.GameRedisRepository;
import com.a405.gamept.play.repository.FightingEnemyRedisRepository;
import com.a405.gamept.play.repository.PlayerRedisRepository;
import com.a405.gamept.util.ValidateUtil;

import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FightServiceImpl implements FightService {

    private final MonsterRepository monsterRepository;
    private final StoryRepository storyRepository;
    private final GameRedisRepository gameRedisRepository;
    private final PlayerRedisRepository playerRedisRepository;
    private final FightingEnemyRedisRepository fightingEnemyRedisRepository;
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final ItemRepository itemRepository;
    private final ItemStatRepository itemStatRepository;
    private final ActRepository actRepository;
    private final StatRepository statRepository;

    public int getRandomMonsterLevel(String storyCode, int playerLevel) throws GameException {
        Story story = storyRepository.findById(storyCode)
                .orElseThrow(() -> new GameException(GameErrorMessage.STORY_NOT_FOUND));
        int monsterLevel = playerLevel;

        int randNum = (int) Math.floor(Math.random() * GameData.MAX_PERCENTAGE);  // 몬스터 레벨 정비 확률
        int sumPercentage = 0; // GameData.MAX_PERCENTAGE을 0으로 초기화

        for(int i : GameData.monsterRate.keySet()) {
            sumPercentage += GameData.monsterRate.get(i);  // 각 정비 확률 더하기

            if(randNum <= sumPercentage) {  // 해당 확률에 속할 시
                monsterLevel += i;
                break;
            }
        }
        // 레벨 조정
        monsterLevel = Math.max(1, monsterLevel); monsterLevel = Math.min(GameData.MONSTER_MAX_LEVEL, monsterLevel);

        return monsterLevel;
    }

    @Override
    @Transactional
    public MonsterGetResponseDto getMonster(MonsterSetCommandDto monsterSetCommandDto) {
        ValidateUtil.validate(monsterSetCommandDto);

        Game game = gameRedisRepository.findById(monsterSetCommandDto.gameCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));

        FightingEnemy fightingEnemy = null;
        if(game.getFightingEnemyCode() == null || game.getFightingEnemyCode().trim().equals("")) {
            fightingEnemy = setMonster(monsterSetCommandDto);
        } else {
            fightingEnemy = fightingEnemyRedisRepository.findById(game.getFightingEnemyCode())
                    .orElseThrow(() -> new GameException(GameErrorMessage.FIGHTING_ENEMY_INVALID));
        }

        MonsterGetResponseDto monsterGetResponseDto = MonsterGetResponseDto.from(fightingEnemy);
        ValidateUtil.validate(monsterGetResponseDto);

        return monsterGetResponseDto;
    }

    private FightingEnemy setMonster(MonsterSetCommandDto monsterSetCommandDto) {
        Game game = gameRedisRepository.findById(monsterSetCommandDto.gameCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));

        if(game.getFightingEnemyCode() != null && !game.getFightingEnemyCode().trim().equals("")) {
            throw new GameException(GameErrorMessage.FIGHTING_ENEMY_FULL);
        }

        Monster monster = null;
        if(game.getTurn() < 30) {
            Player player = playerRedisRepository.findById(monsterSetCommandDto.playerCode())
                    .orElseThrow(() -> new GameException(GameErrorMessage.PLAYER_NOT_FOUND));

            int monsterLevel = getRandomMonsterLevel(game.getStoryCode(), player.getLevel());

            monster = monsterRepository.findByStoryCodeAndLevel(game.getStoryCode(), monsterLevel)
                    .orElseThrow(() -> new GameException(GameErrorMessage.MONSTER_INVALID));
            //log.info("등장 몬스터: { 레벨: " + monster.getLevel() + " 공격력 : " + monster.getAttack() + " }");
        } else {
            monster = monsterRepository.findById("MON-011")
                    .orElseThrow(() -> new GameException(GameErrorMessage.MONSTER_INVALID));
            //log.info("마왕 등장: { 레벨: " + monster.getLevel() + " 공격력 : " + monster.getAttack() + " }");
        }

        String code = "";
        do {
            code = ValidateUtil.getRandomUID();
        } while (gameRedisRepository.findById(code).isPresent());

        FightingEnemy fightingEnemy = fightingEnemyRedisRepository.save(FightingEnemy.builder()
                .code(code)
                .level(monster.getLevel())
                .hp(monster.getHp())
                .attack(monster.getAttack())
                .build());

        fightingEnemyRedisRepository.save(fightingEnemy);
        gameRedisRepository.save(game.toBuilder().fightingEnemyCode(fightingEnemy.getCode()).build());

        return fightingEnemy;
    }

    /*
    public String setMonster(Monster monster) {
        // 몬스터 임의 코드 생성
        String code = "";
        do {
            code = ValidateUtil.getRandomUID();
        } while(fightingEnemyRedisRepository.findById(code).isPresent());

        getMonster();

        FightingEnemy fightingEnemy = FightingEnemy.builder()
                .code(code)
                .level(monster.getLevel())
                .hp(monster.getHp())
                .attack(monster.getAttack())
                .build();

        fightingEnemyRedisRepository.save(fightingEnemy);

        return code;
    }
    */

    @Override
    public FightResultGetResponseDto getFightResult(FightResultGetCommandDto fightResultGetCommandDto) {
        Game game = gameRedisRepository.findById(fightResultGetCommandDto.gameCode())
                .orElseThrow(()->new GameException(GameErrorMessage.GAME_NOT_FOUND));
        Player player = playerRedisRepository.findById(fightResultGetCommandDto.playerCode())
                .orElseThrow(()->new GameException(GameErrorMessage.PLAYER_NOT_FOUND));
        FightingEnemy fightingEnemy = fightingEnemyRedisRepository.findById(game.getFightingEnemyCode())
                .orElseThrow(()->new GameException(GameErrorMessage.GAME_NOT_FOUND));

        String actCode = fightResultGetCommandDto.actCode();
        Subtask subtask = fightResultGetCommandDto.subtask();

        // 데미지를 주면 얼마나 데미지를 줬는지 몬스터의 피는 얼마나 남았는지
        // 공격 받았으면 얼마나 데미지를 받았고 피는 얼마나 남았는지
        int diceValue = game.getDiceValue();
        GameStateCommandDto gameStateCommandDto = new GameStateCommandDto("", "N", player.getHp(), player.getLevel(), player.getStatPoint(), player.getExp(), fightingEnemy.getHp());

        if(subtask.equals(Subtask.NONE)){
            if(actCode.equals("ACT-001")){
                //기본공격
                 gameStateCommandDto = basicAttack(player, fightingEnemy,diceValue);
            }else if(actCode.equals("ACT-004")){
                //도망가기
                gameStateCommandDto = runaway(player, fightingEnemy, actCode, diceValue);
            }
        }else if(subtask.equals(Subtask.SKILL)){
            String skillCode = fightResultGetCommandDto.actCode();
            if(skillCode.equals("SKILL-004") || skillCode.equals("SKILL-008") || skillCode.equals("SKILL-012") || skillCode.equals("SKILL-016")){
                //방어 스킬
                gameStateCommandDto = defence(player, fightingEnemy, skillCode, diceValue);
            }else{
                //공격 스킬
                gameStateCommandDto = skillAttack(player, fightingEnemy, skillCode, diceValue);
            }
        }else if(subtask.equals(Subtask.ITEM)){
            // 아이템 사용
            String itemCode = fightResultGetCommandDto.actCode();
            gameStateCommandDto = itemUse(player, fightingEnemy, itemCode);
        }

        String endYn = gameStateCommandDto.endYn();

        //플레이어 저장
        int playerHp = gameStateCommandDto.playerHp();
        int playerLevel = gameStateCommandDto.playerLevel();
        int statPoint = gameStateCommandDto.statPoint();
        int playerExp = gameStateCommandDto.playerExp();
        player = player.toBuilder()
                .hp(playerHp)
                .level(playerLevel)
                .statPoint(statPoint)
                .exp(playerExp)
                .build();
        playerRedisRepository.save(player);

        // 몬스터 변경 사항 저장
        int fightEnemyHp = gameStateCommandDto.fightEnemyHp();
        if(fightEnemyHp == 0 || endYn.equals("Y")){
            fightingEnemyRedisRepository.delete(fightingEnemy);
            game = game.toBuilder()
                            .fightingEnemyCode("")
                                    .build();
            clearStat(player);
        }else {
            fightingEnemy = fightingEnemy.toBuilder()
                    .hp(fightEnemyHp)
                    .build();
            fightingEnemyRedisRepository.save(fightingEnemy);
        }

        game = game.toBuilder()
                .diceValue(0)
                .build();

        gameRedisRepository.save(game);

        //log.info("전투 끝 : "+gameStateCommandDto.prompt());
        return FightResultGetResponseDto.from(gameStateCommandDto);
    }

    public void clearStat(Player player){
        Map<String, Integer> statList = player.getStat();
        Map<String, Integer> tmpStatList = new HashMap<>();
        if(player.getTmpAddStat() != null){
            tmpStatList = player.getTmpAddStat();
        }

        for(String statCode : tmpStatList.keySet()){
            statList.put(statCode, statList.get(statCode) - tmpStatList.get(statCode));
            tmpStatList.remove(statCode);
        }

        player = player.toBuilder()
                .stat(statList)
                .tmpAddStat(tmpStatList)
                .build();
        playerRedisRepository.save(player);
    }
    //기본공격
    public GameStateCommandDto basicAttack(Player player, FightingEnemy fightingEnemy, int diceValue){
        //log.info("기본 공격 시작");
        // 플레이어의 직업의 주 스탯에 따라 주사위 값과 합산한 값을 몬스터에 준다
        Job job = jobRepository.findById(player.getJobCode())
                .orElseThrow(()->new GameException(GameErrorMessage.JOB_NOT_FOUND));
        StringBuilder result = new StringBuilder();

        //메인 스탯 값 찾기
        String mainStatCode = job.getStat().getCode();
        int mainstatValue = player.getStat().get(mainStatCode);

        // 지존우석은 00 만큼의 데미지를 적에게 입혔다
        // 적의 HP : 00
        int damage = diceValue + plusPoint(mainstatValue);
        int fightingEnemyHp = Math.max(fightingEnemy.getHp() - damage, 0);

        result.append(player.getNickname()).append(" 은 적에게 ").append(damage).append("피해를 입혔다!!!\n");
        result.append("적의 HP : ").append(fightingEnemyHp).append("\n");

        int playerHp = player.getHp();
        String nickname = player.getNickname();
        int playerLevel = player.getLevel();
        int statPoint = player.getStatPoint();
        int playerExp = player.getExp();
        int fightingEnemyAttack = fightingEnemy.getAttack();
        int fightingEnemyLevel = fightingEnemy.getLevel();

        PlayerStateCommandDto playerStateCommandDto = gameOverCheck(playerHp, nickname, playerLevel, statPoint, playerExp, fightingEnemyHp, fightingEnemyAttack, fightingEnemyLevel);

        result.append(playerStateCommandDto.prompt());
        playerHp = playerStateCommandDto.playerHp();
        playerLevel = playerStateCommandDto.playerLevel();
        statPoint = playerStateCommandDto.statPoint();
        playerExp = playerStateCommandDto.playerExp();
        String endYn = FightEndCheck(playerHp, fightingEnemyHp);

        return GameStateCommandDto.of(result.toString(), endYn, playerHp, playerLevel, statPoint, playerExp, fightingEnemyHp);
    }

    public String FightEndCheck(int playerHp, int fightEnemyHp){
        if(playerHp == 0 || fightEnemyHp == 0) return "Y";

        return "N";
    }

    //도망가기
    public GameStateCommandDto runaway(Player player, FightingEnemy fightingEnemy, String actCode, int diceValue){
        //플레이어의 민첩 스텟에 따라 성공여부 확인
        //log.info("도망 시작");
        // 민첩에 영향을 받아 성공 여부 파악 후 진행
        StringBuilder result = new StringBuilder();
        Act act = actRepository.findById(actCode)
                .orElseThrow(()->new GameException(GameErrorMessage.ACT_NOT_FOUND));

        String statCode = act.getStat().getCode();
        int playerStat = player.getStat().get(statCode);
        int bonusPoint = plusPoint(playerStat);
        int successStd = act.getSuccessStd();

        int playerHp = player.getHp();
        String nickname = player.getNickname();
        int playerLevel = player.getLevel();
        int statPoint = player.getStatPoint();
        int playerExp = player.getExp();
        int fightingEnemyHp = fightingEnemy.getHp();
        int fightingEnemyAttack = fightingEnemy.getAttack();
        int fightingEnemyLevel = fightingEnemy.getLevel();
        String endYn = "Y";

        PlayerStateCommandDto playerStateCommandDto = null;
        if(successStd > diceValue+bonusPoint) {
            //실패
            result.append("나중에 보자!!!\n");
            result.append("적 : 그냥 지금 보자!!\n");
            result.append(player.getNickname()).append(" 은 적에게서 도망치려고 했지만 붙잡혀버렸다....\n");

            playerStateCommandDto = gameOverCheck(playerHp, nickname, playerLevel, statPoint, playerExp, fightingEnemyHp, fightingEnemyAttack, fightingEnemyLevel);

            result.append(playerStateCommandDto.prompt());
            playerHp = playerStateCommandDto.playerHp();
            playerLevel = playerStateCommandDto.playerLevel();
            statPoint = playerStateCommandDto.statPoint();
            playerExp = playerStateCommandDto.playerExp();
            endYn = FightEndCheck(playerHp, fightingEnemyHp);
        }else {
            //성공
            result.append("두고 보자!!!\n");
            result.append(player.getNickname()).append(" 은 적에게서 성공적으로 도망쳤다!!!!");
        }

        return GameStateCommandDto.of(result.toString(), endYn, playerHp, playerLevel, statPoint, playerExp, fightingEnemyHp);
    }
    //스킬
    public GameStateCommandDto skillAttack(Player player, FightingEnemy fightingEnemy, String skillCode, int diceValue){
        //log.info("공격 스킬 사용 시작");
        // 플레이어의 스탯과 주사위 값에 따라 스킬 성공여부 파악
        Skill skill = skillRepository.findById(skillCode)
                .orElseThrow(()->new GameException(GameErrorMessage.SKILL_NOT_FOUND));
        StringBuilder result = new StringBuilder();

        //스킬 성공 여부 확인
        SkillSuccessCommandDto skillSuccessCommandDto = skillSuccessCheck(skill, player, diceValue);
        boolean extremeFlag = skillSuccessCommandDto.extremeFlag();
        boolean successFlag = skillSuccessCommandDto.successFlag();

        //추가 데미지 계산
        int bonusDamage = 0;
        if(successFlag){
            bonusDamage = (extremeFlag? GameData.SECOND_BONUS:GameData.FIRST_BONUS) * 10;
        }else{
            bonusDamage = (extremeFlag? GameData.FIRST_DEMERIT:0) * 10;
        }

        // 사용 성공 시 데미지를 준다 -> 방어의 경우 대성공일때 완전 방어
        // 실패 시 데미지를 주지 못한다. 플레이어의 피가 깍일 수 있음
        // 실패시 보너스 데미지 만큼 데미지를 받는다
        int skillDamage = skill.getDamage();
        int damage = 0;
        int fightingEnemyHp = fightingEnemy.getHp();
        int playerHp = player.getHp();

        result.append(player.getNickname()).append("가 ").append(skill.getName()).append(" 를 사용했다!!!\n");

        if(successFlag){
            if(extremeFlag) {
                result.append("대성공!!!\n");
            }else {
                result.append("성공!!!!\n");
            }

            damage = skillDamage + bonusDamage;
            fightingEnemyHp = Math.max(fightingEnemyHp - damage, 0);

            result.append(player.getNickname()).append(" 은 적에게 ").append(damage).append("피해를 입혔다!!!\n");
            result.append("적의 HP : ").append(fightingEnemyHp).append("\n");
        }else {
            if(extremeFlag) {
                result.append("대실패!!!\n");
                damage = bonusDamage;
                //log.info("플레이어 HP 전: "+playerHp);
                playerHp = Math.max(playerHp+damage, 0);

                result.append(player.getNickname()).append(" 은 대실패의 여파로 ").append(damage).append("피해를 입었다!!!\n");
                result.append("적의 HP : ").append(fightingEnemyHp).append("\n");
            }else {
                result.append("실패!!!!\n");

                result.append(player.getNickname()).append(" 은 스킬을 사용하는데 실패했다....");
                result.append("적의 HP : ").append(fightingEnemyHp).append("\n");
            }
        }

        //log.info("플레이어 HP 후: "+playerHp);
        //log.info("플레이어 HP 저장 후: "+player.getHp());
        String nickname = player.getNickname();
        int playerLevel = player.getLevel();
        int statPoint = player.getStatPoint();
        int playerExp = player.getExp();
        int fightingEnemyAttack = fightingEnemy.getAttack();
        int fightingEnemyLevel = fightingEnemy.getLevel();

        PlayerStateCommandDto playerStateCommandDto = gameOverCheck(playerHp, nickname, playerLevel, statPoint, playerExp, fightingEnemyHp, fightingEnemyAttack, fightingEnemyLevel);

        result.append(playerStateCommandDto.prompt());
        playerHp = playerStateCommandDto.playerHp();
        playerLevel = playerStateCommandDto.playerLevel();
        statPoint = playerStateCommandDto.statPoint();
        playerExp = playerStateCommandDto.playerExp();
        String endYn = FightEndCheck(playerHp, fightingEnemyHp);

        return GameStateCommandDto.of(result.toString(), endYn, playerHp, playerLevel, statPoint, playerExp, fightingEnemyHp);
    }

    public GameStateCommandDto defence(Player player, FightingEnemy fightingEnemy, String skillCode, int diceValue){
        //log.info("방어 스킬 사용 시작");
        // 플레이어의 스탯과 주사위 값에 따라 스킬 성공여부 파악
        Skill skill = skillRepository.findById(skillCode)
                .orElseThrow(()->new GameException(GameErrorMessage.SKILL_NOT_FOUND));
        StringBuilder result = new StringBuilder();

        //방어 성공 여부 확인
        SkillSuccessCommandDto skillSuccessCommandDto = skillSuccessCheck(skill, player, diceValue);
        boolean extremeFlag = skillSuccessCommandDto.extremeFlag();
        boolean successFlag = skillSuccessCommandDto.successFlag();

        //추가 데미지 계산
        int bonusDefesive = 0;
        if(successFlag){
            bonusDefesive = (extremeFlag? GameData.SECOND_BONUS:GameData.FIRST_BONUS) * 10;
        }else{
            bonusDefesive = (extremeFlag? GameData.FIRST_BONUS:0) * 10;
        }

        //방어 사용
        int defesivePower = skill.getDamage();
        int damage = 0;
        int fightingEnemyHp = fightingEnemy.getHp();
        int fightingEnemyAttack = fightingEnemy.getAttack();
        int playerHp = player.getHp();

        result.append(player.getNickname()).append("가 ").append(skill.getName()).append(" 를 사용했다!!!\n");
        if (successFlag){
            if (extremeFlag){
                //대성공
                // 공격을 팅겨내서 데미지를 줌
                int defensiveDamage = defesivePower + bonusDefesive;
                fightingEnemyHp = Math.max(fightingEnemyHp-defensiveDamage, 0);

                result.append("대성공!!!\n");
                result.append(player.getNickname()).append("은/는 적의 공격을 팅겨냈다!!!\n");
                result.append("적에게 ").append(defensiveDamage).append(" 만큼의 충격을 돌려주었다!!!\n");
                result.append("적의 HP : ").append(fightingEnemyHp).append("\n");
            }else {
                // 공격을 좀 막아줌
                damage = Math.max(fightingEnemyAttack - (defesivePower + bonusDefesive), 0);
                result.append("성공!!!\n");

                if(damage > 0) {
                    result.append("적에게 ").append(defesivePower + bonusDefesive)
                            .append(" 만큼의 충격을 막아냈다!!!\n");
                    result.append("적에게서 ").append(damage).append(" 만큼의 충격을 받았다.\n");
                }else{
                    result.append(player.getNickname()).append("은/는 적의 공격을 막았다!!!\n");
                }
                result.append("적의 HP : ").append(fightingEnemyHp).append("\n");
            }
        }else {
            if (extremeFlag){
                //대성공
                // 적의 공격 + 실패 데미지
                damage = fightingEnemyAttack + bonusDefesive;
                result.append("대실패!!!\n");
                result.append(player.getNickname()).append("은/는 적의 공격을 막으려 했지만 대실패의 여파로 더욱 큰 충격을 받았다!!!\n");
                result.append("적에게 ").append(damage).append(" 만큼의 충격을 받았다!!!\n");
                result.append("적의 HP : ").append(fightingEnemyHp).append("\n");
            }else{
                damage = fightingEnemyAttack;
                result.append("실패!!!\n");
                result.append(player.getNickname()).append("은/는 적의 공격을 막으려 했지만 막을 수 없었다!!!\n");
                result.append("적에게 ").append(damage).append(" 만큼의 충격을 받았다!!!\n");
                result.append("적의 HP : ").append(fightingEnemyHp).append("\n");
            }
        }

        playerHp = Math.max(playerHp-damage, 0);
        String endYn = "N";
        String nickname = player.getNickname();
        int playerLevel = player.getLevel();
        int statPoint = player.getStatPoint();
        int playerExp = player.getExp();
        int fightingEnemyLevel = fightingEnemy.getLevel();

        if(playerHp == 0){
            //게임 오버
            result.append("HP가 0이 되어버렸다...\n");
            result.append(player.getNickname()).append("눈앞이 깜깜해진다.....\n");
            endYn = "Y";

        }else if(fightingEnemyHp == 0){
            // 전투 종료
            result.append(player.getNickname()).append(" 은 적을 ").append(damage).append("무사히 쓰러트렸다!!!\n");
            ExpGetCommandDto expGetCommandDto = getExp(playerLevel, statPoint, playerExp, nickname, fightingEnemyLevel);
            endYn = "Y";
            result.append(expGetCommandDto.prompt());
            playerLevel = expGetCommandDto.playerLevel();
            playerExp = expGetCommandDto.playerExp();
            statPoint = expGetCommandDto.statPoint();
        }

        return GameStateCommandDto.of(result.toString(), endYn, playerHp, playerLevel, statPoint, playerExp, fightingEnemyHp);
    }
    //아이템 사용
    public GameStateCommandDto itemUse(Player player, FightingEnemy fightingEnemy, String itemCode){
        // 플레이어가 가진 아이템 목록에서 아이템을 사용
        //log.info("아이템 사용 시작");
        StringBuilder result = new StringBuilder();
        boolean haveItemFlag = false;
        List<String> itemCodeList = player.getItemCodeList();
        for(String code : itemCodeList){
            if(code.equals(itemCode)) {
                haveItemFlag = true;
                itemCodeList.remove(code);
                break;
            }
        }

        if(!haveItemFlag) throw new GameException(GameErrorMessage.ITEM_INVALID);

        Item item = itemRepository.findById(itemCode)
                .orElseThrow(()->new GameException(GameErrorMessage.ITEM_INVALID));

        ItemCase itemCase = item.getItemCase();
        String itemName = item.getName();
        // 아이템의 데미지, 스탯업, 힐 량 가져오지
        ItemStat itemStat = itemStatRepository.findByItemCode(itemCode)
                .orElseThrow(()->new GameException(GameErrorMessage.ITEM_STAT_NOT_FOUND));
        int itemEffectValue = itemStat.getEffectValue();

        int playerHp = player.getHp();
        String nickname = player.getNickname();
        String endYn = "Y";
        int playerLevel = player.getLevel();
        int playerExp = player.getExp();
        int statPoint = player.getStatPoint();
        PlayerStateCommandDto playerStateCommandDto = new PlayerStateCommandDto(nickname, playerHp, playerLevel, playerExp, statPoint, "");
        int fightingEnemyHp = fightingEnemy.getHp();
        int fightingEnemyAttack = fightingEnemy.getAttack();
        int fightingEnemyLevel = fightingEnemy.getLevel();
        //아이템 종류 별 발동
        if(itemCase.equals(ItemCase.STAT)){
            result.append("로이더!!!로이더!!!\n");
            result.append(player.getNickname()).append(" 은/는 스탯 향상 아이템 ").append(itemName).append("을/를 사용했다!\n");
            Map<String, Integer> playerStatList = player.getStat();
            Map<String, Integer> tmpStatList = new HashMap<>();
            if(player.getTmpAddStat() != null){
                tmpStatList = player.getTmpAddStat();
            }

            String statCode = itemStat.getStat().getCode();
            Stat stat = statRepository.findById(statCode)
                    .orElseThrow(()->new GameException(GameErrorMessage.STAT_INVALID));
            String statName = stat.getName();
            result.append(statName).append("이 ").append(itemEffectValue).append("만큼 상승합니다!!!!");

            // 변화된 스탯 입력
            playerStatList.put(statCode, playerStatList.get(statCode)+ itemEffectValue);
            tmpStatList.put(statCode, itemEffectValue);

            //스탯 저장
            player = player.toBuilder()
                    .stat(playerStatList)
                    .tmpAddStat(tmpStatList)
                    .build();

            playerRedisRepository.save(player);

            result.append("전투가 종료까지 효과가 지속됩니다...");
            playerStateCommandDto = gameOverCheck(playerHp, nickname, playerLevel, statPoint, playerExp, fightingEnemyHp, fightingEnemyAttack, fightingEnemyLevel);
        } else if(itemCase.equals(ItemCase.DAMAGE)){
            //log.info("데미지 주는 아이템");
            int damage = itemEffectValue;
            int bonusPoint = 0;

            result.append("파이어 인 더 홀!!!!!!\n");
            result.append(player.getNickname()).append(" 은/는 투척 아이템 ").append(itemName).append("을/를 사용했다!\n");

            String statCode = itemStat.getStat().getCode();
            Stat stat = statRepository.findById(statCode)
                    .orElseThrow(()->new GameException(GameErrorMessage.STAT_INVALID));
            String statName = stat.getName();
            int playerStatFromItem = player.getStat().get(statCode);

            bonusPoint = plusPoint(playerStatFromItem);
            if(bonusPoint > 0) result.append(statCode).append("의 영향으로 ").append(itemName).append("의 위력이 ").append(bonusPoint).append("만큼 상승합니다!!!\n");

            damage += bonusPoint;
            fightingEnemyHp = fightingEnemy.getHp() - damage;

            result.append(player.getNickname()).append(" 은/는 적에게 ").append(damage).append("의 피해를 입혔다!\n");
            result.append("적의 체력 : ").append(fightingEnemyHp);

            playerStateCommandDto = gameOverCheck(playerHp, nickname, playerLevel, statPoint, playerExp, fightingEnemyHp, fightingEnemyAttack, fightingEnemyLevel);
        } else if(itemCase.equals(ItemCase.HEAL)){
            int playerMaxHp = player.getStat().get("STAT-001") * 10;
            playerHp = Math.min(playerMaxHp, player.getHp() + itemEffectValue);

            result.append("회복 아이템 사용!!!\n");
            result.append(player.getNickname()).append(" 은/는 회복 아이템 ").append(itemName).append("을/를 사용했다!\n");
            if(playerMaxHp == playerHp) result.append("HP가 모두 회복되었다.\n");
            else {
                result.append("HP가 ").append(itemEffectValue).append("회복되었다.\n");
            }
            result.append("현재 체력 : ").append(playerHp).append("\n");
            playerStateCommandDto = gameOverCheck(playerHp, nickname, playerLevel, statPoint, playerExp, fightingEnemyHp, fightingEnemyAttack, fightingEnemyLevel);
        } else if (itemCase.equals(ItemCase.ESCAPE)) {
            result.append("연막탄 사용!!!\n");
            result.append(player.getNickname()).append(" 은/는 ").append(itemName).append("을/를 사용했다!\n");
            result.append("적에게서 안전하게 도망쳤다...");
            endYn = "Y";
        }

        result.append(playerStateCommandDto.prompt());
        playerHp = playerStateCommandDto.playerHp();
        playerLevel = playerStateCommandDto.playerLevel();
        statPoint = playerStateCommandDto.statPoint();
        playerExp = playerStateCommandDto.playerExp();
        endYn = FightEndCheck(playerHp, fightingEnemyHp);

        player = player.toBuilder()
                .itemCodeList(itemCodeList)
                .hp(playerHp)
                .build();
        playerRedisRepository.save(player);

        return GameStateCommandDto.of(result.toString(), endYn, playerHp, playerLevel, statPoint, playerExp, fightingEnemyHp);
    }


    public SkillSuccessCommandDto skillSuccessCheck(Skill skill, Player player, int diceValue){
        //log.info("스킬 성공 여부 확인");
        int successStd = skill.getSuccessStd();
        int extremePoint = skill.getExtremeStd();

        int playerStat = player.getStat().get(skill.getStat().getCode());
        int playerStd = diceValue + plusPoint(playerStat);

        boolean extremeFlag = false;
        int bonusDamage = 0;

        //대성공 대실패 여부
        if(successStd + extremePoint <= playerStd || successStd - extremePoint >= playerStd){
            extremeFlag = true;
        }

        // 성공 여부 확인
        boolean successFlag = successStd <= playerStd;

        return new SkillSuccessCommandDto(extremeFlag, successFlag);
    }

    public PlayerStateCommandDto gameOverCheck(int playerHp, String nickname, int playerLevel, int statPoint, int playerExp, int fightingEnemyHp, int fightingEnemyAttack, int fightingEnemyLevel){
        //log.info("공격, 공격 받은 후 데미지 계산하기 - 게임 오버 체크");
        StringBuilder result = new StringBuilder();
        String endYn = "N";

        if(playerHp <= 0){
            //게임 오버
            result.append("HP가 0이 되어버렸다...\n");
            result.append(nickname).append(" 은 쓰러지고 말았다.\n");
            result.append("눈앞이 깜깜해진다.....\n");
            endYn = "Y";
        }else if(fightingEnemyHp == 0){
            // 전투 종료
            result.append(nickname).append(" 은 적을 무사히 쓰러트렸다!!!\n");
            ExpGetCommandDto expGetCommandDto = getExp(playerLevel, statPoint, playerExp, nickname, fightingEnemyLevel);
            result.append(expGetCommandDto.prompt());
            playerLevel = expGetCommandDto.playerLevel();
            playerExp = expGetCommandDto.playerExp();
            statPoint = expGetCommandDto.statPoint();
            endYn = "Y";
        }else {
            // 공격 받아야함
            AttackedCommandDto attacked = attacked(playerHp, nickname, fightingEnemyAttack);
            result.append(attacked.prompt());
            playerHp = attacked.playerHp();
            endYn = attacked.endYn();
        }

        return PlayerStateCommandDto.builder()
                .playerHp(playerHp)
                .playerLevel(playerLevel)
                .playerExp(playerExp)
                .statPoint(statPoint)
                .prompt(result.toString())
                .build();
    }

    public int plusPoint(int playerMainStat){
        //log.info("스탯에 따른 보너스 포인트 확인");
        // 보너스 포인트 계산
        int plusPoint = 0;

        if(playerMainStat >= GameData.THIRD_STEP){
            plusPoint = GameData.THIRD_BONUS;
        } else if(playerMainStat >= GameData.SECOND_STEP){
            plusPoint = GameData.SECOND_BONUS;
        } else if(playerMainStat >= GameData.FIRST_STEP){
            plusPoint = GameData.FIRST_BONUS;
        }

        return plusPoint;
    }
    //몬스터 공격
    public AttackedCommandDto attacked(int playerHp, String nickname, int fightingEnemyAttack){
        //log.info("몬스터의 공격을 받기 시작했다.");
        StringBuilder result = new StringBuilder();

        int damage = fightingEnemyAttack;
        playerHp = Math.max(playerHp - damage, 0);

        result.append("적이 공격해온다!!!!\n");
        result.append(nickname).append(" 은 적에게 ").append(damage).append("의 공격을 받았다!!!\n");
        result.append("남은 체력 : ").append(playerHp).append("\n");
        //log.info("몬스터의 공격을 받은 후 : "+playerHp);

        if(playerHp <= 0){
            result.append("HP가 0이 되어버렸다...\n");
            result.append(nickname).append(" 은 쓰러지고 말았다.\n").append("눈앞이 깜깜해진다.....\n");
        }

        String endYn = playerHp <= 0?"Y":"N";

        return AttackedCommandDto.builder()
                .prompt(result.toString())
                .playerHp(playerHp)
                .endYn(endYn)
                .build();
    }

    //경험치 획득 처리
    public ExpGetCommandDto getExp(int playerLevel, int statPoint, int playerExp, String nickname, int fightingEnemyLevel){
        StringBuilder result = new StringBuilder();

        if(playerLevel < 10){
            int expStd = playerLevel - fightingEnemyLevel;
            int exp = 0;
            if(expStd == 0) exp = 5;
            else if(expStd == -2) exp = 7;
            else if(expStd == -1) exp = 6;
            else if(expStd == 2) exp = 3;
            else if(expStd == 1) exp = 4;

            playerExp += exp;


            result.append(nickname).append("은/는 ").append(exp*10).append("의 경험치를 얻었다!!!!\n");
            if(playerExp >= 10){
                playerExp = playerExp - 10;
                if (playerLevel + 1 < GameData.PLAYER_MAX_LEVEL){
                    playerLevel++;
                    statPoint++;
                    result.append(nickname).append("레벨이 올랐다\n");
                    result.append(playerLevel).append("이/가 되었다.\n");
                    result.append("스탯 포인트가 생겼다!!!!\n");
                    result.append("잔여 스탯 포인트 : ").append(statPoint).append("\n");
                }
            }
        }else{
            result.append(nickname).append("은/는 이미 충분히 강하다!!!\n");
            result.append("더 이상 레벨이 오르지 않는다.\n");
        }

        return new ExpGetCommandDto(playerExp, playerLevel, statPoint, result.toString());
    }
}
