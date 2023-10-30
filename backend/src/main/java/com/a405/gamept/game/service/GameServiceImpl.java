package com.a405.gamept.game.service;


import com.a405.gamept.game.dto.command.RaceGetCommandDto;
import com.a405.gamept.game.dto.response.RaceGetResponseDto;
import com.a405.gamept.game.entity.Race;
import com.a405.gamept.game.entity.Story;
import com.a405.gamept.game.repository.RaceRepository;
import com.a405.gamept.game.repository.StoryRepository;
import com.a405.gamept.game.util.exception.GameException;
import com.a405.gamept.game.util.enums.GameErrorMessage;
import jakarta.validation.ConstraintViolation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class GameServiceImpl implements GameService {
    private final Validator validator;
    Set<ConstraintViolation<Object>> violations;
    private final RaceRepository raceRepository;
    private final StoryRepository storyRepository;

    @Autowired
    public GameServiceImpl(RaceRepository raceRepository, StoryRepository storyRepository) {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.raceRepository = raceRepository;
        this.storyRepository = storyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RaceGetResponseDto> getRaceList(RaceGetCommandDto raceGetCommandDto) throws GameException {
        Story story = storyRepository.findById(raceGetCommandDto.storyCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.STORY_NOT_FOUND));

        List<Race> raceList = raceRepository.findAllByStory(story)
                .orElseThrow(() -> new GameException(GameErrorMessage.RACE_INVALID));

        if(raceList.size() == 0) {
            throw new GameException(GameErrorMessage.RACE_INVALID);
        }

        // DTO 리스트로 변환
        List<RaceGetResponseDto> raceGetResponseDtoList = new ArrayList<>();
        for (Race race : raceList) {  // 각각의 DTO에 대해 유효성 검사 진행
            raceGetResponseDtoList.add(RaceGetResponseDto.from(race));

            // 유효성 검사
            violations = validator.validate(raceGetResponseDtoList.get(raceGetResponseDtoList.size() - 1));

            if (!violations.isEmpty()) {  // 유효성 검사 실패 시
                for (ConstraintViolation<Object> violation : violations) {
                    throw new GameException(violation.getMessage());
                }
            }
        }

        return raceGetResponseDtoList;
    }
}
