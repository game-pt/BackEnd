package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.JobGetCommandDto;
import com.a405.gamept.game.dto.command.RaceGetCommandDto;
import com.a405.gamept.game.dto.response.JobGetResponseDto;
import com.a405.gamept.game.dto.response.RaceGetResponseDto;
import com.a405.gamept.game.entity.Job;
import com.a405.gamept.game.entity.Race;
import com.a405.gamept.game.entity.Story;
import com.a405.gamept.game.repository.JobRepository;
import com.a405.gamept.game.repository.RaceRepository;
import com.a405.gamept.game.repository.StoryRepository;
import com.a405.gamept.game.util.enums.GameErrorMessage;
import com.a405.gamept.game.util.exception.GameException;
import com.a405.gamept.play.entity.Game;
import com.a405.gamept.play.repository.GameRedisRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class GameStartServiceImpl implements GameStartService {

    private final Validator validator;
    Set<ConstraintViolation<Object>> violations;
    private final GameRedisRepository gameRepository;
    private final StoryRepository storyRepository;
    private final RaceRepository raceRepository;
    private final JobRepository jobRepository;

    @Autowired
    public GameStartServiceImpl(GameRedisRepository gameRepository, StoryRepository storyRepository, RaceRepository raceRepository, JobRepository jobRepository) {
        this.gameRepository = gameRepository;
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.storyRepository = storyRepository;
        this.raceRepository = raceRepository;
        this.jobRepository = jobRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RaceGetResponseDto> getRaceList(RaceGetCommandDto raceGetCommandDto) throws GameException {
        Game game = gameRepository.findById(raceGetCommandDto.gameCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));

        // 현재 게임의 스토리 조회
        Story story = storyRepository.findById(game.getStoryCode())
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

    @Override
    @Transactional(readOnly = true)
    public List<JobGetResponseDto> getJobList(JobGetCommandDto jobGetCommandDto) throws GameException {
        Game game = gameRepository.findById(jobGetCommandDto.gameCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));

        // 현재 게임의 스토리 조회
        Story story = storyRepository.findById(game.getStoryCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.STORY_NOT_FOUND));

        List<Job> jobList = jobRepository.findAllByStory(story)
                .orElseThrow(() -> new GameException(GameErrorMessage.JOB_INVALID));
        if(jobList.size() == 0) {
            throw new GameException(GameErrorMessage.JOB_INVALID);
        }
        // DTO 리스트로 변환
        List<JobGetResponseDto> jobGetResponseDtoList = new ArrayList<>();
        for (Job job : jobList) {  // 각각의 DTO에 대해 유효성 검사 진행
            jobGetResponseDtoList.add(JobGetResponseDto.from(job));

            // 유효성 검사
            violations = validator.validate(jobGetResponseDtoList.get(jobGetResponseDtoList.size() - 1));

            if (!violations.isEmpty()) {  // 유효성 검사 실패 시
                for (ConstraintViolation<Object> violation : violations) {
                    throw new GameException(violation.getMessage());
                }
            }
        }
        return jobGetResponseDtoList;
    }
}
