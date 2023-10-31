package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.JobGetCommandDto;
import com.a405.gamept.game.dto.command.PlayerSetCommandDto;
import com.a405.gamept.game.dto.command.RaceGetCommandDto;
import com.a405.gamept.game.dto.response.JobGetResponseDto;
import com.a405.gamept.game.dto.response.PlayerSetResponseDto;
import com.a405.gamept.game.dto.response.RaceGetResponseDto;
import com.a405.gamept.game.entity.*;
import com.a405.gamept.game.repository.JobRepository;
import com.a405.gamept.game.repository.RaceRepository;
import com.a405.gamept.game.repository.StoryRepository;
import com.a405.gamept.game.util.FinalData;
import com.a405.gamept.game.util.enums.GameErrorMessage;
import com.a405.gamept.game.util.exception.GameException;
import com.a405.gamept.play.entity.Game;
import com.a405.gamept.play.entity.Player;
import com.a405.gamept.play.repository.GameRedisRepository;
import com.a405.gamept.util.ValidateUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class PlayerServiceImpl implements PlayerService {
    private final GameRedisRepository gameRepository;
    private final StoryRepository storyRepository;
    private final RaceRepository raceRepository;
    private final JobRepository jobRepository;

    @Autowired
    public PlayerServiceImpl(GameRedisRepository gameRepository, StoryRepository storyRepository, RaceRepository raceRepository, JobRepository jobRepository) {
        this.gameRepository = gameRepository;
        this.storyRepository = storyRepository;
        this.raceRepository = raceRepository;
        this.jobRepository = jobRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RaceGetResponseDto> getRaceList(RaceGetCommandDto raceGetCommandDto) throws GameException {
        ValidateUtil.validate(raceGetCommandDto);

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
        for (Race race : raceList) {
            raceGetResponseDtoList.add(RaceGetResponseDto.from(race));
            // 유효성 검사
            ValidateUtil.validate(raceGetResponseDtoList.get(raceGetResponseDtoList.size() - 1));
        }
        return raceGetResponseDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobGetResponseDto> getJobList(JobGetCommandDto jobGetCommandDto) throws GameException {
        ValidateUtil.validate(jobGetCommandDto);

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
        for (Job job : jobList) {
            jobGetResponseDtoList.add(JobGetResponseDto.from(job));
            // 유효성 검사
            ValidateUtil.validate(jobGetResponseDtoList.get(jobGetResponseDtoList.size() - 1));
        }
        return jobGetResponseDtoList;
    }

    @Override
    public PlayerSetResponseDto setPlayer(PlayerSetCommandDto playerSetCommandDto) throws GameException {
        ValidateUtil.validate(playerSetCommandDto);

        Game game = gameRepository.findById(playerSetCommandDto.gameCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));

        // 플레이어 코드를 위한 현재 플레이어 순서
        int playerNum = game.getPlayerList().size() + 1;
        Race race = raceRepository.findById(playerSetCommandDto.raceCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.RACE_NOT_FOUND));
        Job job = jobRepository.findById(playerSetCommandDto.jobCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.JOB_NOT_FOUND));

        // 스탯 설정
        Map<String, Integer> stat = new HashMap<>();

        int statValue;
        for (RaceStat raceStat : race.getRaceStatList()) {
            for (JobBonus jobBonus : job.getJobBonusList()) {  // 종족 별 스탯과 직업 별 추가 스탯 순회
                if(raceStat.getStat().equals(jobBonus.getStat())) {  // 종족별 스탯과 직업 별 추가 스탯이 일치할 경우
                    statValue = raceStat.getStatValue() + jobBonus.getStatBonus();  // 연산
                    if(statValue < 0) statValue = 0;
                    else if(FinalData.MAX_STAT < statValue) statValue = FinalData.MAX_STAT;  // 숫자 범위 처리

                    stat.put(raceStat.getStat().getCode(), statValue);  // 플레이어 스탯에 삽입
                    break;
                }
            }
        }

        log.info("생성된 플레이어 스탯: " + stat);


        Player player = Player.builder()
                .code("PLAYER" + playerNum)  // 임의의 코드 생성
                .race(race)
                .job(job)
                .nickname(playerSetCommandDto.nickname())
                .stat(stat)
                .build();

        PlayerSetResponseDto playerSetResponseDto = PlayerSetResponseDto.from(player);
        ValidateUtil.validate(playerSetResponseDto);  // 유효성 검사

        return playerSetResponseDto;
    }
}
