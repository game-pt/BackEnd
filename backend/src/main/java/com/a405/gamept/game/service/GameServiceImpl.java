package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.ActGetCommandDto;
import com.a405.gamept.game.dto.command.ActResultGetCommandDto;
import com.a405.gamept.game.dto.command.DiceGetCommandDto;
import com.a405.gamept.game.dto.command.GameSetCommandDto;
import com.a405.gamept.game.dto.command.StoryGetCommandDto;
import com.a405.gamept.game.dto.command.SubtaskCommandDto;
import com.a405.gamept.game.dto.response.ActGetResponseDto;
import com.a405.gamept.game.dto.response.DiceGetResponseDto;
import com.a405.gamept.game.dto.response.GameSetResponseDto;
import com.a405.gamept.game.dto.response.PromptResultGetResponseDto;
import com.a405.gamept.game.dto.response.StoryGetResponseDto;
import com.a405.gamept.game.dto.response.SubtaskResponseDto;
import com.a405.gamept.game.entity.Act;
import com.a405.gamept.game.entity.ActStat;
import com.a405.gamept.game.entity.Item;
import com.a405.gamept.game.entity.Skill;
import com.a405.gamept.game.entity.Story;
import com.a405.gamept.game.entity.Subtask;
import com.a405.gamept.game.repository.ActRepository;
import com.a405.gamept.game.repository.ActStatRepository;
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
    private final ActStatRepository actStatRepository;
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
        int diceValue = dice1+dice2+dice3;
        log.info(dice1+"\t"+dice2+"\t"+dice3+"\t총합 : "+diceValue);

        DiceGetResponseDto diceResult = DiceGetResponseDto.of(dice1, dice2, dice3);

        ValidateUtil.validate(diceResult);

        Game game = gameRedisRepository.findById(diceGetCommandDto.gameCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));
        game = game.toBuilder().diceValue(diceValue).build();
        gameRedisRepository.save(game);

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
        if(!gameCheck(actGetCommandDto.gameCode(), actGetCommandDto.playerCode())){
            log.error("DiceInvalidException: { PlayerError : player Code를 확인해주세요. }");
        }

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
                subtaskResponseDtoList.add(SubtaskResponseDto.of(skill.getCode(), skill.getName(), skill.getDesc()));

                ValidateUtil.validate(subtaskResponseDtoList.get(subtaskResponseDtoList.size()-1));
            }
        }else if(subtask.getKey().equals("ITEM")){
            for(Item item : player.getItemList()){
                subtaskResponseDtoList.add(SubtaskResponseDto.of(item.getCode(), item.getName(), item.getDesc()));
            }
        }
        return subtaskResponseDtoList;
    }

    @Override
    public PromptResultGetResponseDto playAct(ActResultGetCommandDto actResultGetCommandDto) {
        Act act = actRepository.findById(actResultGetCommandDto.actCode())
                .orElseThrow(()->new GameException(GameErrorMessage.ACT_NOT_FOUND));
        Game game = gameRedisRepository.findById(actResultGetCommandDto.gameCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));
        Player player = playerRedisRepository.findById(actResultGetCommandDto.playerCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.PLAYER_NOT_FOUND));

        // 다이스 값 가져오기
        int diceValue = game.getDiceValue();

        //보너스 스탯
        String statName = act.getStat().getName();
        int relationStat = player.getStat().get(statName);
        int extremePoint = act.getExtremeStd();
        int plusPoint = 0;

        if(relationStat >= 16){
            plusPoint = 3;
        } else if(relationStat >= 12){
            plusPoint = 2;
        } else if(relationStat >= 8){
            plusPoint = 1;
        }
        //성공 기준 값
        int successStd = act.getSuccessStd();
        int playerStd = (diceValue + plusPoint);

        //성공, 실패에 따른 진행
        StringBuilder prompt = new StringBuilder();
        String eventName = act.getEvent().getName();

        // 극적 성공, 실패 여부 확인
        boolean flag = false;
        int bonusPoint = 0;

        //대성공 대실패 여부
        if(successStd + extremePoint <= playerStd || successStd - extremePoint >= playerStd){
               flag = true;
               prompt.append("매우 ");
        }

        // 성공 여부 확인
        if(successStd <= playerStd){
            bonusPoint = flag?2:1;
            prompt.append("성공적인 ");
        }else{
            bonusPoint = flag?-1:0;
            prompt.append("성공적이지 못한 ");
        }

        // 스탯 변화 진행

        // 아이템 획득
        return null;
    }

    public static void extremeSuccess(){

    }

    public static void extremeFail(){

    }

    public static int rest(Player player){
        int hp = player.getHp();
        int healthPoint = player.getStat().get("건강");
        int maxHp = healthPoint * 10;

        return Math.min(hp + 20, maxHp);
    }
}
