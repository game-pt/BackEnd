package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.DeathCheckCommandDto;
import com.a405.gamept.game.dto.command.FightResultGetCommandDto;
import com.a405.gamept.game.dto.command.MonsterGetCommandDto;
import com.a405.gamept.game.dto.command.SkillSuccessCommandDto;
import com.a405.gamept.game.dto.response.FightResultGetResponseDto;
import com.a405.gamept.game.dto.response.MonsterGetResponseDto;
import com.a405.gamept.game.entity.Job;
import com.a405.gamept.game.entity.Monster;
import com.a405.gamept.game.entity.Skill;
import com.a405.gamept.game.entity.Story;
import com.a405.gamept.game.entity.Subtask;
import com.a405.gamept.game.repository.JobRepository;
import com.a405.gamept.game.repository.MonsterRepository;
import com.a405.gamept.game.repository.SkillRepository;
import com.a405.gamept.game.repository.StoryRepository;
import com.a405.gamept.game.util.GameData;
import com.a405.gamept.game.util.enums.GameErrorMessage;
import com.a405.gamept.game.util.exception.GameException;
import com.a405.gamept.play.entity.Game;
import com.a405.gamept.play.entity.Gmonster;
import com.a405.gamept.play.entity.Player;
import com.a405.gamept.play.repository.GameRedisRepository;
import com.a405.gamept.play.repository.GmonsterRedisRepository;
import com.a405.gamept.play.repository.PlayerRedisRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class FightServiceImpl implements FightService {

    private final Validator validator;
    Set<ConstraintViolation<Object>> violations;
    private final MonsterRepository monsterRepository;
    private final StoryRepository storyRepository;
    private final GameRedisRepository gameRedisRepository;
    private final PlayerRedisRepository playerRedisRepository;
    private final GmonsterRedisRepository gmonsterRedisRepository;
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    @Override
    public MonsterGetResponseDto getMonster(MonsterGetCommandDto monsterGetCommandDto) throws GameException {
        Story story = storyRepository.findById(monsterGetCommandDto.storyCode()).orElseThrow(() -> new GameException(GameErrorMessage.MONSTER_INVALID));

        /** 몬스터 레벨 난수 뽑기 **/
        int sum = 0;  // 랜덤하게 돌릴 숫자
        for(int i : GameData.monsterRate.values()) {
            sum += i;  // 각 확률 더하기
        }

        log.info("몬스터 등장확률 총합: " + sum);
        int randNum = (int) Math.floor(Math.random() * sum);  // 몬스터 레벨 확률

        sum = 0;  // 각 확률 더하기
        int monsterLevel = 0;
        for(int key : GameData.monsterRate.keySet()) {
            sum += GameData.monsterRate.get(key);  // 확률 더하기
            if(randNum < sum) {  // 확률에 해당할 경우
                monsterLevel = monsterGetCommandDto.playerLevel() + key;
                if(monsterLevel <= 0){  // 몬스터 레벨이 0 이하일 경우
                    monsterLevel = 1;  // 몬스터 레벨 1로 설정
                } else if(GameData.MONSTER_MAX_LEVEL < monsterLevel) {  // 몬스터 레벨이 10 초과일 경우
                    monsterLevel = GameData.MONSTER_MAX_LEVEL;  // 몬스터 레벨 10으로 설정
                }
                break;
            }
        }

        Monster monster = monsterRepository.findByStoryCodeAndLevel(monsterGetCommandDto.storyCode(), monsterLevel)
                .orElseThrow(()->new GameException(GameErrorMessage.MONSTER_INVALID));
        log.info("등장 몬스터: { 레벨: " + monster.getLevel() + " 공격력 : "+ monster.getAttack()+" }");

        //몬스터 생성
        String gmonsterCode = monsterCreat(monster);
        MonsterGetResponseDto monsterGetResponseDto = MonsterGetResponseDto.from(monster, gmonsterCode);

        // 유효성 검사
        violations = validator.validate(monsterGetResponseDto);

        if (!violations.isEmpty()) {  // 유효성 검사 실패 시
            for (ConstraintViolation<Object> violation : violations) {
                throw new GameException(violation.getMessage());
            }
        }

        return monsterGetResponseDto;
    }

    public String monsterCreat(Monster monster){
        log.info("Redis에 몬스터 생성");
        // 몬스터 임의 코드 생성
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String code = new Random().ints(6, 0, CHARACTERS.length())
                .mapToObj(CHARACTERS::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());

        Gmonster gmonster = Gmonster.builder()
                .code(code)
                .level(monster.getLevel())
                .hp(monster.getAttack())
                .attack(monster.getAttack())
                .build();

        gmonsterRedisRepository.save(gmonster);

        return code;
    }
    @Override
    public FightResultGetResponseDto getFightResult(FightResultGetCommandDto fightResultGetCommandDto) {
        Game game = gameRedisRepository.findById(fightResultGetCommandDto.gameCode())
                .orElseThrow(()->new GameException(GameErrorMessage.GAME_NOT_FOUND));
        Player player = playerRedisRepository.findById(fightResultGetCommandDto.playerCode())
                .orElseThrow(()->new GameException(GameErrorMessage.PLAYER_NOT_FOUND));
        Gmonster gmonster = gmonsterRedisRepository.findById(fightResultGetCommandDto.gmonsterCode())
                .orElseThrow(()->new GameException(GameErrorMessage.GAME_NOT_FOUND));

        String actCode = fightResultGetCommandDto.actCode();
        Subtask subtask = fightResultGetCommandDto.subtask();

        // 데미지를 주면 얼마나 데미지를 줬는지 몬스터의 피는 얼마나 남았는지
        // 공격 받았으면 얼마나 데미지를 받았고 피는 얼마나 남았는지
        int diceValue = game.getDiceValue();
        DeathCheckCommandDto deathCheckCommandDto = null;
        if(subtask.equals(Subtask.NONE)){
            if(actCode.equals("ACT-001")){
                deathCheckCommandDto.from(basicAttack(player,gmonster,diceValue));
            }else if(actCode.equals("ACT-004")){

            }
        }else if(subtask.equals(Subtask.SKILL)){
            String skillCode = fightResultGetCommandDto.actCode();
            if(skillCode.equals("SKILL-004") || skillCode.equals("SKILL-008") || skillCode.equals("SKILL-012") || skillCode.equals("SKILL-016")){
                //defence();
            }else{
                skillAttack(player, gmonster, skillCode, diceValue);
            }
        }else if(subtask.equals(Subtask.ITEM)){

        }

        game.toBuilder()
                .diceValue(0)
                .build();


        gameRedisRepository.save(game);
        playerRedisRepository.save(player);


        return null;
    }

    //기본공격
    public DeathCheckCommandDto basicAttack(Player player, Gmonster gmonster, int diceValue){
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
        int gmonsterHp = Math.max(gmonster.getHp() - damage, 0);
        String endYn = "N";

        result.append(player.getNickname()).append(" 은 적에게 ").append(damage).append("피해를 입혔다!!!\n");
        result.append("적의 HP : ").append(gmonsterHp).append("\n");

        gmonster.toBuilder()
                .hp(gmonsterHp)
                .build();
        gmonsterRedisRepository.save(gmonster);

        int playerHp = player.getHp();
        if(gmonsterHp == 0){
            result.append(player.getNickname()).append(" 은 적을 ").append(damage).append("무사히 쓰러트렸다!!!\n");
            endYn = "Y";
        }else{
            DeathCheckCommandDto deathCheckCommandDto = attacted(player, gmonster);
            result.append(deathCheckCommandDto.prompt());
            endYn = deathCheckCommandDto.endYn();
            playerHp = deathCheckCommandDto.playerHp();
        }

        return new DeathCheckCommandDto(result.toString(), endYn, playerHp);
    }

    //스킬
    public DeathCheckCommandDto skillAttack(Player player, Gmonster gmonster, String skillCode, int diceValue){
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
        int gmonsterHp = gmonster.getHp();
        int playerHp = player.getHp();

        result.append(player.getNickname()).append("가 ").append(skill.getName()).append(" 를 사용했다!!!\n");

        if(successFlag){
            if(extremeFlag) {
                result.append("대성공!!!\n");
            }else {
                result.append("성공!!!!\n");
            }

            damage = skillDamage + bonusDamage;
            gmonsterHp = Math.max(gmonsterHp - damage, 0);

            result.append(player.getNickname()).append(" 은 적에게 ").append(damage).append("피해를 입혔다!!!\n");
            result.append("적의 HP : ").append(gmonsterHp).append("\n");

            gmonster.toBuilder()
                    .hp(gmonsterHp)
                    .build();
            gmonsterRedisRepository.save(gmonster);

        }else {
            if(extremeFlag) {
                result.append("대실패!!!\n");
                damage = bonusDamage;
                playerHp = Math.max(playerHp-damage, 0);

                result.append(player.getNickname()).append(" 은 대실패의 여파로 ").append(damage).append("피해를 입었다!!!\n");
                result.append("적의 HP : ").append(gmonsterHp).append("\n");

            }else {
                result.append("실패!!!!\n");

                result.append(player.getNickname()).append(" 은 스킬을 사용하는데 실패했다....");
                result.append("적의 HP : ").append(gmonsterHp).append("\n");
            }
        }

        String endYn = "N";
        if(playerHp == 0){
            //게임 오버
            result.append("HP가 0이 되어버렸다...\n");
            result.append(player.getNickname()).append(" 은 쓰러지고 말았다.\n").append(damage).append("눈앞이 깜깜해진다.....\n");
            endYn = "Y";
        }else if(gmonsterHp == 0){
            // 전투 종료
            result.append(player.getNickname()).append(" 은 적을 ").append(damage).append("무사히 쓰러트렸다!!!\n");
            endYn = "Y";
        }else {
            DeathCheckCommandDto deathCheckCommandDto = attacted(player, gmonster);
            result.append(deathCheckCommandDto.prompt());
            endYn = deathCheckCommandDto.endYn();
        }

        return new DeathCheckCommandDto(result.toString(), endYn, playerHp);
    }
    public void defence(Player player, Gmonster gmonster, String skillCode, int diceValue){
        // 플레이어의 스탯과 주사위 값에 따라 스킬 성공여부 파악
        Skill skill = skillRepository.findById(skillCode)
                .orElseThrow(()->new GameException(GameErrorMessage.SKILL_NOT_FOUND));
        StringBuilder result = new StringBuilder();

        //방어 성공 여부 확인
        SkillSuccessCommandDto skillSuccessCommandDto = skillSuccessCheck(skill, player, diceValue);
        boolean extremeFlag = skillSuccessCommandDto.extremeFlag();
        boolean successFlag = skillSuccessCommandDto.successFlag();

        //추가 데미지 계산
        int bonusDamage = 0;
        if(successFlag){
            bonusDamage = (extremeFlag? GameData.SECOND_BONUS:GameData.FIRST_BONUS) * 10;
        }else{
            bonusDamage = (extremeFlag? GameData.FIRST_BONUS:0) * 10;
        }

        //방어 사용
        result.append(player.getNickname()).append("가 ").append(skill.getName()).append(" 를 사용했다!!!\n");
        if (successFlag){
            if (extremeFlag){
                //대성공
                // 공격을 팅겨내서 데미지를 줌
                result.append("대성공!!!\n");

            }else {
                // 공격을 좀 막아줌
                result.append("성공!!!\n");
            }
        }else {
            if (extremeFlag){
                //대성공
                // 적의 공격 + 실패 데미지
                result.append("대실패!!!\n");

            }else{
                result.append("실패!!!\n");
            }
        }
    }

    public SkillSuccessCommandDto skillSuccessCheck(Skill skill, Player player, int diceValue){
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

    //데미지 계산
    public int plusPoint(int playerMainStat){
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

    //아이템 사용
    public void itemUse(Player player){
        // 플레이어가 가진 아이템 목록에서 아이템을 사용
    }

    //도망가기
    public void runaway(Player player, String actCode){
        //플레이어의 민첩 스텟에 따라 성공여부 확인

    }

    //몬스터 공격
    public DeathCheckCommandDto attacted(Player player, Gmonster gmonster){
        StringBuilder result = new StringBuilder();

        int damage = gmonster.getAttack();
        int playerHp = player.getHp() - damage;
        String endYn = "N";
        result.append(player.getNickname()).append(" 은 적에게 ").append(damage).append("의 공격을 받았다!!!\n");

        player.toBuilder()
                .hp(playerHp)
                .build();
        playerRedisRepository.save(player);

        if(playerHp == 0){
            result.append("HP가 0이 되어버렸다...\n");
            result.append(player.getNickname()).append(" 은 쓰러지고 말았다.\n").append(damage).append("눈앞이 깜깜해진다.....\n");
            endYn = "Y";
        }

        return new DeathCheckCommandDto(result.toString(), endYn, playerHp);
    }

    //경험치 획득 처리
    public void expGet(){

    }
}
