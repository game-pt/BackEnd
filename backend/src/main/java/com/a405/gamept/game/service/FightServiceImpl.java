package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.MonsterGetCommandDto;
import com.a405.gamept.game.dto.response.MonsterGetResponseDto;
import com.a405.gamept.game.entity.Monster;
import com.a405.gamept.game.entity.Story;
import com.a405.gamept.game.repository.MonsterRepository;
import com.a405.gamept.game.repository.StoryRepository;
import com.a405.gamept.game.util.FinalData;
import com.a405.gamept.game.util.exception.GameInvalidException;
import com.a405.gamept.game.util.exception.MonsterInvalidException;
import com.a405.gamept.global.error.exception.InternalServerException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class FightServiceImpl implements FightService {

    private final Validator validator;
    Set<ConstraintViolation<Object>> violations;
    private final MonsterRepository monsterRepository;
    private final StoryRepository storyRepository;

    @Autowired
    public FightServiceImpl(Validator validator, MonsterRepository monsterRepository, StoryRepository storyRepository) {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.monsterRepository = monsterRepository;
        this.storyRepository = storyRepository;
    }

    @Override
    public MonsterGetResponseDto getMonster(MonsterGetCommandDto monsterGetCommandDto) throws InternalServerException {
        Story story = storyRepository.findById(monsterGetCommandDto.storyCode()).orElseThrow(GameInvalidException::new);
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
                int playerLevel = monsterGetCommandDto.level() + key;
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

        MonsterGetResponseDto monsterGetResponseDto = MonsterGetResponseDto.from(monster);

        // 유효성 검사
        violations = validator.validate(monsterGetResponseDto);

        if (!violations.isEmpty()) {  // 유효성 검사 실패 시
            for (ConstraintViolation<Object> violation : violations) {
                log.error("MonsterInvalidException: { FightService " + violation.getMessage() + " }");
            }
            throw new MonsterInvalidException();
        }

        return monsterGetResponseDto;
    }
}
