package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.ActGetCommandDto;
import com.a405.gamept.game.dto.command.DiceGetCommandDto;
import com.a405.gamept.game.dto.command.GameSetCommandDto;
import com.a405.gamept.game.dto.command.StoryGetCommandDto;
import com.a405.gamept.game.dto.command.SubtaskCommandDto;
import com.a405.gamept.game.dto.response.ActGetResponseDto;
import com.a405.gamept.game.dto.response.DiceGetResponseDto;
import com.a405.gamept.game.dto.response.GameSetResponseDto;
import com.a405.gamept.game.dto.response.StoryGetResponseDto;
import com.a405.gamept.game.dto.response.SubtaskResponseDto;
import com.a405.gamept.game.entity.Act;
import com.a405.gamept.game.entity.Item;
import com.a405.gamept.game.entity.Skill;
import com.a405.gamept.game.entity.Story;
import com.a405.gamept.game.entity.Subtask;
import com.a405.gamept.game.repository.ActRepository;
import com.a405.gamept.game.repository.SkillRepository;
import com.a405.gamept.game.repository.StoryRepository;
import com.a405.gamept.game.util.exception.GameException;
import com.a405.gamept.game.util.enums.GameErrorMessage;
import com.a405.gamept.play.entity.Game;
import com.a405.gamept.play.entity.Player;
import com.a405.gamept.play.repository.GameRedisRepository;
import com.a405.gamept.play.repository.PlayerRedisRepository;
import com.a405.gamept.util.ValidateUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.*;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService {

    private final GameRedisRepository gameRedisRepository;
    private final PlayerRedisRepository playerRedisRepository;
    private final ActRepository actRepository;
    private final StoryRepository storyRepository;
    private final SkillRepository skillRepository;

    @Override
    public List<StoryGetResponseDto> getStoryList() {
        List<Story> storyList = storyRepository.findAll();

        List<StoryGetResponseDto> storyGetResponseDtoList = new ArrayList<>();

        for(Story story : storyList) {
            storyGetResponseDtoList.add(StoryGetResponseDto.from(story));
            ValidateUtil.validate(storyGetResponseDtoList.get(storyGetResponseDtoList.size() - 1));
        }
        return storyGetResponseDtoList;
    }

    @Override
    public StoryGetResponseDto getStory(StoryGetCommandDto storyGetCommandDto) {
        ValidateUtil.validate(storyGetCommandDto);

        Story story = storyRepository.findById(storyGetCommandDto.storyCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.STORY_NOT_FOUND));

        StoryGetResponseDto storyGetResponseDto = StoryGetResponseDto.from(story);
        ValidateUtil.validate(storyGetResponseDto);

        return storyGetResponseDto;
    }

    @Override
    public GameSetResponseDto setGame(GameSetCommandDto gameSetCommandDto) {
        ValidateUtil.validate(gameSetCommandDto);

        Story story = storyRepository.findById(gameSetCommandDto.storyCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.STORY_NOT_FOUND));
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String code = new Random().ints(6, 0, CHARACTERS.length())
                .mapToObj(CHARACTERS::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());

        log.info("게임 코드: " + code);

        Game game = Game.builder()
                .code(code)
                .storyCode(story.getCode())
                .build();
        ValidateUtil.validate(game);
        gameRedisRepository.save(game);

        GameSetResponseDto gameSetResponseDto = GameSetResponseDto.from(game);
        ValidateUtil.validate(gameSetResponseDto);

        return gameSetResponseDto;
    }

    @Override
    public DiceGetResponseDto rollOfDice(DiceGetCommandDto diceGetCommandDto) {
        // 랜덤 객체 생성
        Random random = new Random();

        // 1에서 6까지의 랜덤 숫자 생성
        int dice1 = random.nextInt(6) + 1;
        int dice2 = random.nextInt(6) + 1;
        int dice3 = random.nextInt(6) + 1;
        log.info(dice1+"\t"+dice2+"\t"+dice3);

        DiceGetResponseDto diceResult = DiceGetResponseDto.of(dice1, dice2, dice3);

        ValidateUtil.validate(diceResult);

        return DiceGetResponseDto.of(dice1, dice2, dice3);
    }

    @Override
    public boolean gameCheck(String gameCode, String playerCode){
        Game game = gameRedisRepository.findById(gameCode)
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));
        Player player = playerRedisRepository.findById(playerCode)
                .orElseThrow(() -> new GameException(GameErrorMessage.PLAYER_NOT_FOUND));

        List<String> playerList = game.getPlayerList();
        for(String p : playerList){
            if (p.equals(playerCode)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<ActGetResponseDto> getOptions(ActGetCommandDto actGetCommandDto) {
//        if(!gameCheck(actGetCommandDto.gameCode(), actGetCommandDto.playerCode())){
//            log.error("DiceInvalidException: { PlayerError : player Code를 확인해주세요. }");
//        }

        List<Act> actList = actRepository.findAllByEventCode(actGetCommandDto.eventCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.INVALID_ACT_REQUEST));

        List<ActGetResponseDto> actGetResponseDtoList = new ArrayList<>();

        for(Act act : actList){
            actGetResponseDtoList.add(ActGetResponseDto.of(act));

            ValidateUtil.validate(actGetResponseDtoList.get(actGetResponseDtoList.size()-1));
        }
        return actGetResponseDtoList;
    }

    @Override
    public List<SubtaskResponseDto> getSubtask(SubtaskCommandDto subtaskCommandDto) {
        Player player = playerRedisRepository.findById(subtaskCommandDto.playerCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.PLAYER_NOT_FOUND));

        Subtask subtask = subtaskCommandDto.subtask();
        List<SubtaskResponseDto> subtaskResponseDtoList = new ArrayList<>();

        if(subtask.getKey().equals("SKILL")){
            List<Skill> skillList = skillRepository.findAllByJobCode(player.getJobCode());

            for (Skill skill : skillList){
                subtaskResponseDtoList.add(SubtaskResponseDto.of(skill.getCode(), skill.getName()));

                ValidateUtil.validate(subtaskResponseDtoList.get(subtaskResponseDtoList.size()-1));
            }
        }else if(subtask.getKey().equals("ITEM")){
            for(Item item : player.getItemList()){
                subtaskResponseDtoList.add(SubtaskResponseDto.of(item.getCode(), item.getName()));
            }
        }
        return subtaskResponseDtoList;
    }
}
