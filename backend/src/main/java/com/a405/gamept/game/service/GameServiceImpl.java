package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.DiceGetCommandDto;
import com.a405.gamept.game.dto.command.MonsterGetCommandDto;
import com.a405.gamept.game.dto.response.DiceGetResponseDto;
import com.a405.gamept.game.dto.response.MonsterGetResponseDto;
import com.a405.gamept.game.entity.Monster;
import com.a405.gamept.game.entity.Story;
import com.a405.gamept.game.repository.MonsterRepository;
import com.a405.gamept.game.repository.StoryRepository;
import com.a405.gamept.game.util.FinalData;
import com.a405.gamept.game.util.exception.GameInvalidException;
import com.a405.gamept.game.util.exception.MonsterInvalidException;
import com.a405.gamept.global.error.enums.ErrorMessage;
import com.a405.gamept.global.error.exception.custom.BusinessException;
import com.a405.gamept.play.entity.Game;
import com.a405.gamept.play.entity.Player;
import com.a405.gamept.play.repository.GameRedisRepository;
import com.a405.gamept.play.repository.PlayerRedisRepository;
import java.util.Optional;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

    private final MonsterRepository monsterRepository;
    private final StoryRepository storyRepository;
    private final GameRedisRepository gameRedisRepository;
    private final PlayerRedisRepository playerRedisRepository;

    @Autowired
    public GameServiceImpl(MonsterRepository monsterRepository, StoryRepository storyRepository, GameRedisRepository gameRedisRepository, PlayerRedisRepository playerRedisRepository) {
        this.monsterRepository = monsterRepository;
        this.storyRepository = storyRepository;
        this.gameRedisRepository = gameRedisRepository;
        this.playerRedisRepository = playerRedisRepository;
    }

    @Override
    public MonsterGetResponseDto getMonster(MonsterGetCommandDto monsterGetCommandDto) throws GameInvalidException, MonsterInvalidException {
        Story story = storyRepository.findById(monsterGetCommandDto.getStoryCode()).orElseThrow(GameInvalidException::new);
        List<Monster> monsterList = null;

        /** 몬스터 레벨 난수 뽑기 **/
        int sum = 0;  // 랜덤하게 돌릴 숫자
        for(int i : FinalData.monsterRate.values()) {
            sum += i;  // 각 확률 더하기
        }

        log.info("몬스터 등장확률 총합: " + sum);
        int randNum = (int) Math.floor(Math.random() * sum);  // 몬스터 레벨 확률

        sum = 0;
        for(int key : FinalData.monsterRate.keySet()) {
            sum += FinalData.monsterRate.get(key);  // 확률 더하기
            if(randNum < sum) {  // 확률에 해당할 경우
                int playerLevel = monsterGetCommandDto.getLevel() + key;
                if(playerLevel < 0) playerLevel = 0;
                else if(10 < playerLevel) playerLevel = 10;
                monsterList = monsterRepository.findAllByStoryAndLevel(story, playerLevel);
                break;
            }
        }

        // 레벨에 해당하는 몬스터가 존재하지 않을 경우
        if(monsterList.size() == 0) throw new MonsterInvalidException();

        // 랜덤한 몬스터 뽑기
        Monster monster = monsterList.get((int) Math.floor(Math.random() * monsterList.size()));
        log.info("등장 몬스터: { 이름: " + monster.getName() + ", 레벨: " + monster.getLevel() + " }");


        return MonsterGetResponseDto.builder()
                .name(monster.getName())
                .level(monster.getLevel())
                .build();
    }

    @Override
    public DiceGetResponseDto rollOfDice(DiceGetCommandDto diceGetCommandDto) {
        // 랜덤 객체 생성
        Random random = new Random();

        // 1에서 6까지의 랜덤 숫자 생성
        int dice1 = random.nextInt(6) + 1;
        int dice2 = random.nextInt(6) + 1;

        return DiceGetResponseDto.of(dice1, dice2);
    }

    @Override
    public boolean gameCheck(String gameCode, String playerCode){
        Game game = gameRedisRepository.findById(gameCode)
                .orElseThrow(() -> new BusinessException(ErrorMessage.GAME_NOT_FOUND));
        Player player = playerRedisRepository.findById(playerCode)
                .orElseThrow(() -> new BusinessException(ErrorMessage.PLAYER_NOT_FOUND));

        List<Player> playerList = game.getPlayerList();
        boolean pass = false;
        for(Player p : playerList){
            if (p.getCode().equals(playerCode)) {
                pass = true;
                break;
            }
        }

        return pass;
    }
}
