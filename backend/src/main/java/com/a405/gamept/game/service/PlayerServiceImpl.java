package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.JobGetCommandDto;
import com.a405.gamept.game.dto.command.PlayerGetCommandDto;
import com.a405.gamept.game.dto.command.PlayerSetCommandDto;
import com.a405.gamept.game.dto.command.RaceGetCommandDto;
import com.a405.gamept.game.dto.response.*;
import com.a405.gamept.game.entity.*;
import com.a405.gamept.game.repository.*;
import com.a405.gamept.game.util.GameData;
import com.a405.gamept.game.util.enums.GameErrorMessage;
import com.a405.gamept.game.util.exception.GameException;
import com.a405.gamept.play.entity.Game;
import com.a405.gamept.play.entity.Player;
import com.a405.gamept.play.repository.GameRedisRepository;
import com.a405.gamept.play.repository.PlayerRedisRepository;
import com.a405.gamept.util.ValidateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private final GameRedisRepository gameRepository;
    private final PlayerRedisRepository playerRepository;
    private final StoryRepository storyRepository;
    private final RaceRepository raceRepository;
    private final JobRepository jobRepository;
    private final StatRepository statRepository;
    private final ItemRepository itemRepository;

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
    @Transactional
    public PlayerSetResponseDto setPlayer(PlayerSetCommandDto playerSetCommandDto) throws GameException {
        ValidateUtil.validate(playerSetCommandDto);

        Game game = gameRepository.findById(playerSetCommandDto.gameCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));

        // 플레이어 코드를 위한 현재 플레이어 순서
        List<String> playerList = game.getPlayerList();
        if(playerList == null) {
            playerList = new ArrayList<>();
        }

        if(playerList.size() == 4) {
            throw new GameException(GameErrorMessage.PLAYER_FULL);
        }

        int playerNum = playerList.size() + 1;
        Race race = raceRepository.findById(playerSetCommandDto.raceCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.RACE_NOT_FOUND));
        Job job = jobRepository.findById(playerSetCommandDto.jobCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.JOB_NOT_FOUND));

        // 스탯 설정
        Map<String, Integer> stat = new HashMap<>();

        int statValue;
        for (RaceStat raceStat : race.getRaceStatList()) {
            statValue = raceStat.getStatValue();
            for (JobBonus jobBonus : job.getJobBonusList()) {  // 종족 별 스탯과 직업 별 추가 스탯 순회
                if(raceStat.getStat().equals(jobBonus.getStat())) {  // 종족별 스탯과 직업 별 추가 스탯이 일치할 경우
                    statValue += jobBonus.getStatBonus();
                    if(statValue < 0) statValue = 0;
                    else if(GameData.MAX_STAT < statValue) statValue = GameData.MAX_STAT;  // 숫자 범위 처리

                    break;
                }
            }
            stat.put(raceStat.getStat().getCode(), statValue);  // 플레이어 스탯에 삽입
        }
        int hp = stat.get("STAT-001") * 10;
        // 플레이어 임의 코드 생성
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String code = new Random().ints(6, 0, CHARACTERS.length())
                .mapToObj(CHARACTERS::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());

        Player player = Player.builder()
                .code(game.getCode() + "-" + code)  // 임의의 코드 생성
                .raceCode(race.getCode())
                .jobCode(job.getCode())
                .nickname(playerSetCommandDto.nickname())
                .stat(stat)
                .hp(hp)
                .build();

        playerList.add(player.getCode());
        game = game.toBuilder().playerList(playerList).build();

        gameRepository.save(game);
        playerRepository.save(player);

        PlayerSetResponseDto playerSetResponseDto = PlayerSetResponseDto.from(player);
        ValidateUtil.validate(playerSetResponseDto);  // 유효성 검사

        return playerSetResponseDto;
    }

    @Override
    public PlayerGetResponseDto getPlayer(PlayerGetCommandDto playerGetCommandDto) throws GameException {
        ValidateUtil.validate(playerGetCommandDto);

        Game game = gameRepository.findById(playerGetCommandDto.gameCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));

        Player player = playerRepository.findById(playerGetCommandDto.playerCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.PLAYER_NOT_FOUND));

        Race race = raceRepository.findById(player.getRaceCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.RACE_INVALID));

        Job job = jobRepository.findById(player.getJobCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.JOB_INVALID));

        boolean flag = false;  // 방에 존재하는 사용자인지 체크하는 로직
        for (String playerCode : game.getPlayerList()) {
            if(playerCode.equals(player.getCode())) {
                flag = true;
                break;
            }
        }
        if(!flag) {  // 플레이어가 방에 존재하지 않을 경우
            throw new GameException(GameErrorMessage.PLAYER_NOT_FOUND);
        }

        // 스탯 세팅
        List<StatGetResponseDto> statGetResponseDtoList = new ArrayList<>();

        StatGetResponseDto statGetResponseDto;
        for(String stat : player.getStat().keySet()) {
            statGetResponseDto = StatGetResponseDto.from(statRepository.findById(stat)
                    .orElseThrow(() -> new GameException(GameErrorMessage.STAT_INVALID)), player.getStat().get(stat));
            ValidateUtil.validate(statGetResponseDto);

            statGetResponseDtoList.add(statGetResponseDto);
        }

        // 아이템 세팅
        List<ItemGetResponseDto> itemGetResponseDtoList = new ArrayList<>();

        ItemGetResponseDto itemGetResponseDto;
        if(player.getItemCodeList() != null) {
            for(String item : player.getItemCodeList()) {
                itemGetResponseDto = ItemGetResponseDto.from(itemRepository.findById(item)
                        .orElseThrow(() -> new GameException(GameErrorMessage.ITEM_INVALID)));
                ValidateUtil.validate(itemGetResponseDto);

                itemGetResponseDtoList.add(itemGetResponseDto);
            }
        }

        // 플레이어 정보 반환
        PlayerGetResponseDto playerGetResponseDto = PlayerGetResponseDto
                .builder()
                .nickname(player.getNickname())
                .race(PlayerRaceGetResponseDto.from(race))
                .job(PlayerJobGetResponseDto.from(job))
                .hp(player.getHp())
                .exp(player.getExp())
                .statList(statGetResponseDtoList)
                .itemList(itemGetResponseDtoList)
                .build();
        ValidateUtil.validate(playerGetResponseDto);

        return playerGetResponseDto;
    }
}
