package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.*;
import com.a405.gamept.game.dto.response.*;
import com.a405.gamept.game.entity.Act;
import com.a405.gamept.game.entity.ActStat;
import com.a405.gamept.game.entity.Event;
import com.a405.gamept.game.entity.Item;
import com.a405.gamept.game.entity.Skill;
import com.a405.gamept.game.entity.Stat;
import com.a405.gamept.game.entity.Story;
import com.a405.gamept.game.entity.Subtask;
import com.a405.gamept.game.repository.ActRepository;
import com.a405.gamept.game.repository.ActStatRepository;
import com.a405.gamept.game.repository.EventRepository;
import com.a405.gamept.game.repository.ItemRepository;
import com.a405.gamept.game.repository.SkillRepository;
import com.a405.gamept.game.repository.StatRepository;
import com.a405.gamept.game.repository.StoryRepository;
import com.a405.gamept.util.ChatGptClientUtil;
import com.a405.gamept.game.util.GameData;
import com.a405.gamept.game.util.Words;
import com.a405.gamept.game.util.exception.GameException;
import com.a405.gamept.game.util.enums.GameErrorMessage;
import com.a405.gamept.play.entity.Game;
import com.a405.gamept.play.entity.Player;
import com.a405.gamept.play.entity.Prompt;
import com.a405.gamept.play.repository.GameRedisRepository;
import com.a405.gamept.play.repository.PlayerRedisRepository;
import com.a405.gamept.util.ValidateUtil;

import java.text.SimpleDateFormat;
import java.util.*;

import java.util.ArrayList;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ItemRepository itemRepository;
    private final StatRepository statRepository;
    private final ChatGptClientUtil chatGptClientUtil;
    private final EventRepository eventRepository;

    @Override
    public List<StoryGetResponseDto> getStoryList() {
        List<Story> storyList = storyRepository.findAll();

        List<StoryGetResponseDto> storyGetResponseDtoList = new ArrayList<>();

        for (Story story : storyList) {
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
    @Transactional
    public GameSetResponseDto setGame(GameSetCommandDto gameSetCommandDto) {
        ValidateUtil.validate(gameSetCommandDto);

        Story story = storyRepository.findById(gameSetCommandDto.storyCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.STORY_NOT_FOUND));

        String code = "";
        do {
            code = ValidateUtil.getRandomUID();
        } while (gameRedisRepository.findById(code).isPresent());

        // 이벤트 지정
        List<String> eventStrList = new ArrayList<>();
        String eventMemory = "";
        for(Event event : story.getEventList()) {
            eventStrList.add("TRPG 게임은 반드시 " + event.getName() + " 상황이 시작될 때," +
                    "[" + event.getName() + "]라고 출력해주어야 합니다. ");
            eventStrList.add(event.getName() + " 상황이란," +
                    "" + event.getPrompt().replace(".", "") + "는 경우를 말합니다.");
        }


        String memory =
                        "당신은 TRPG의 게임 마스터가 되어 나의 대화에 맞춰 이야기를 이어나가야 합니다.\n " +
                        "나는 판타지 세계의 모험가가 되어 게임을 플레이합니다. \n" +
                        "TPRG 게임은 주로 대화 형식으로 게임이 이루어집니다. \n" +
                        "아래에 제시하는 주제, 이벤트, 글자수에 맞춰 이야기를 이어나가 주세요. \n" +
                        "나의 대답을 당신이 말할 수 없습니다. \n" +
                        "내가 대답할 수 있는 여지를 주어야 합니다. \n" +
                        "당신은 선택지를 줄 수 없습니다. \n" +
                        String.join("\n", eventStrList) + "\n" +
                        "첫 시작은 당신이 안내자의 입장이 되어 나와 만난 것을 반겨줍니다.\n" +
                        "\n주제: " + story.getDesc() + "\n 글자 수 : 최대 100자";

        Game game = Game.builder()
                .code(code)
                .storyCode(story.getCode())
                .memory(memory)
                .promptList(setStartPrompt(memory))
                .build();
        ValidateUtil.validate(game);
        gameRedisRepository.save(game);

        GameSetResponseDto gameSetResponseDto = GameSetResponseDto.from(game);
        ValidateUtil.validate(gameSetResponseDto);

        return gameSetResponseDto;
    }

    private List<Prompt> setStartPrompt(String memory) {
        List<Prompt> list = new ArrayList<>();
        list.add(Prompt.builder()
                .role("user")
                .content("모험을 시작한다.")
                .build());
        list.add(Prompt.builder()
                .role("assistant")
                .content(chatGptClientUtil.getChatGPTResult(memory, list, ""))
                .build());

        list.remove(0);

        return list;
    }

    @Override
    public ChatResponseDto chat(ChatCommandDto chatCommandDto) {
        ValidateUtil.validate(chatCommandDto);

        Game game = gameRedisRepository.findById(chatCommandDto.gameCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));

        Player player = playerRedisRepository.findById(chatCommandDto.playerCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.PLAYER_NOT_FOUND));

        if (!ValidateUtil.validatePlayer(player.getCode(), game.getPlayerList())) {
            throw new GameException(GameErrorMessage.PLAYER_NOT_FOUND);
        }

        String message = chatCommandDto.message().trim();
        SimpleDateFormat time = new SimpleDateFormat("HH:mm", Locale.KOREA);
        // 욕설 처리
        int i = 0;
        for (String b : Words.badWords) {
            i = new Random().nextInt(Words.goodWords.length);
            message = message.replaceAll(b, Words.goodWords[i]);
        }

        String wholeMessage = "[" + time.format(new Date()) + "] " + player.getNickname() + ": " + message;

        ChatResponseDto chatResponseDto = ChatResponseDto.builder()
                .gameCode(game.getCode())
                .message(wholeMessage)
                .build();
        ValidateUtil.validate(chatResponseDto);

        return chatResponseDto;
    }

    @Override
    public DiceGetResponseDto rollOfDice(DiceGetCommandDto diceGetCommandDto) {
        // 랜덤 객체 생성
        Random random = new Random();

        // 1에서 6까지의 랜덤 숫자 생성
        int dice1 = random.nextInt(6) + 1;
        int dice2 = random.nextInt(6) + 1;
        int dice3 = random.nextInt(6) + 1;
        int diceValue = dice1 + dice2 + dice3;
        log.info(dice1 + "\t" + dice2 + "\t" + dice3 + "\t총합 : " + diceValue);

        DiceGetResponseDto diceResult = DiceGetResponseDto.of(dice1, dice2, dice3);

        ValidateUtil.validate(diceResult);

        Game game = gameRedisRepository.findById(diceGetCommandDto.gameCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));
        game = game.toBuilder().diceValue(diceValue).build();
        gameRedisRepository.save(game);

        return DiceGetResponseDto.of(dice1, dice2, dice3);
    }

    @Override
    public boolean gameCheck(String gameCode, String playerCode) {
        Game game = gameRedisRepository.findById(gameCode)
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));
        Player player = playerRedisRepository.findById(playerCode)
                .orElseThrow(() -> new GameException(GameErrorMessage.PLAYER_NOT_FOUND));

        List<String> playerList = game.getPlayerList();
        for (String p : playerList) {
            if (p.equals(playerCode)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<ActGetResponseDto> getOptions(ActGetCommandDto actGetCommandDto) {
        if (!gameCheck(actGetCommandDto.gameCode(), actGetCommandDto.playerCode())) {
            log.error("DiceInvalidException: { PlayerError : player Code를 확인해주세요. }");
        }

        List<Act> actList = actRepository.findAllByEventCode(actGetCommandDto.eventCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.INVALID_ACT_REQUEST));

        List<ActGetResponseDto> actGetResponseDtoList = new ArrayList<>();

        for (Act act : actList) {
            actGetResponseDtoList.add(ActGetResponseDto.of(act));

            ValidateUtil.validate(actGetResponseDtoList.get(actGetResponseDtoList.size() - 1));
        }
        return actGetResponseDtoList;
    }

    @Override
    public List<SubtaskResponseDto> getSubtask(SubtaskCommandDto subtaskCommandDto) {
        Player player = playerRedisRepository.findById(subtaskCommandDto.playerCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.PLAYER_NOT_FOUND));

        Subtask subtask = subtaskCommandDto.subtask();
        List<SubtaskResponseDto> subtaskResponseDtoList = new ArrayList<>();

        if (subtask.getKey().equals("SKILL")) {
            List<Skill> skillList = skillRepository.findAllByJobCode(player.getJobCode());

            for (Skill skill : skillList) {
                subtaskResponseDtoList.add(SubtaskResponseDto.of(skill.getCode(), skill.getName(), skill.getDesc()));

                ValidateUtil.validate(subtaskResponseDtoList.get(subtaskResponseDtoList.size() - 1));
            }
        } else if (subtask.getKey().equals("ITEM")) {
            if (player.getItemCodeList() == null) {
                return subtaskResponseDtoList;
            }
            for (String itemCode : player.getItemCodeList()) {
                Item item = itemRepository.findById(itemCode)
                        .orElseThrow(() -> new GameException(GameErrorMessage.ITEM_INVALID));
                subtaskResponseDtoList.add(SubtaskResponseDto.of(item.getCode(), item.getName(), item.getDesc()));
            }
        }
        return subtaskResponseDtoList;
    }

    @Override
    public ActResultGetResponseDto playAct(ActResultGetCommandDto actResultGetCommandDto) {
        Act act = actRepository.findById(actResultGetCommandDto.actCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.ACT_NOT_FOUND));
        Game game = gameRedisRepository.findById(actResultGetCommandDto.gameCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));
        Player player = playerRedisRepository.findById(actResultGetCommandDto.playerCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.PLAYER_NOT_FOUND));

        // 다이스 값 가져오기
        int diceValue = game.getDiceValue();
        log.info("주사위 값 : "+diceValue);

        //보너스 스탯
        String statCode = act.getStat().getCode();
        int relationStat = player.getStat().get(statCode);
        int extremePoint = act.getExtremeStd();
        int plusPoint = 0;

        if (relationStat >= GameData.THIRD_STEP) {
            plusPoint = GameData.THIRD_BONUS;
        } else if (relationStat >= GameData.SECOND_STEP) {
            plusPoint = GameData.SECOND_BONUS;
        } else if (relationStat >= GameData.FIRST_STEP) {
            plusPoint = GameData.FIRST_BONUS;
        }
        //성공 기준 값
        int successStd = act.getSuccessStd();
        int playerStd = (diceValue + plusPoint);

        log.info("성공 기준치 : "+successStd);
        log.info("극적 기준치 : "+extremePoint);
        //성공, 실패에 따른 진행
        StringBuilder prompt = new StringBuilder();
        //String eventName = act.getEvent().getName();

        prompt.append(player.getNickname()).append("은(는) ");
        // 극적 성공, 실패 여부 확인
        boolean flag = false;
        int bonusPoint = 0;

        //대성공 대실패 여부
        if (successStd + extremePoint <= playerStd || successStd - extremePoint >= playerStd) {
            flag = true;
            prompt.append("매우 ");
        }

        // 성공 여부 확인
        if (successStd <= playerStd) {
            bonusPoint = flag ? 2 : 1;
            prompt.append("성공적인 ");
        } else {
            bonusPoint = flag ? -1 : 0;
            prompt.append("성공적이지 못한 ");
        }
        prompt.append(act.getName()).append("를 했다.");
        log.info(prompt.toString());
        // ChatGPT에 프롬프트 전송
        StringBuilder promptResult = new StringBuilder();
        promptResult.append(prompt).append("\n");
        String gptPrompt = chatGptClientUtil.getChatGPTResult(game.getMemory(), game.getPromptList(), "플레이어인 " + promptResult.toString());
        log.info(gptPrompt);
        promptResult.append(gptPrompt);

        // 스탯 변화 진행
        String tmp = statChane(player, game, act, bonusPoint);
        // 아이템 획득
        String itemYn = "N";
        String gameOverYn = "N";
        String itemCode = "";
        if (tmp == null) {
            //죽음
            playerRedisRepository.delete(player);
            gameRedisRepository.delete(game);
            StringBuilder dead = new StringBuilder();
            dead.append("HP가 0이 되었다.\n");
            dead.append(player.getNickname()).append(" 은 쓰러지고 말았다.\n");
            dead.append("눈앞이 깜깜해진다.....\n");
            gameOverYn = "Y";
        } else {
            Event event = act.getEvent();
            if ((event.getItemYn() == 'Y' && flag) || event.getCode().equals("EV-005")) {
                itemYn = "Y";
                ItemRandomGetCommandDto itemRandomGetCommandDto = getItem(game.getStoryCode(), player);
                promptResult.append("\n").append(itemRandomGetCommandDto.prompt());
                itemCode = itemRandomGetCommandDto.itemCode();
            }
            promptResult.append("\n").append(tmp);
        }
        game = game.toBuilder()
                .diceValue(0)
                .build();
        gameRedisRepository.save(game);
        return ActResultGetResponseDto.of(actResultGetCommandDto.gameCode(), promptResult.toString(), itemYn, itemCode, gameOverYn);
    }

    public String statChane(Player player, Game game, Act act, int bonusPoint) {
        //log.info("상태 변화 진행, 이벤트 : "+act.getEvent().getName()+" 행동 : "+act.getName());
        List<ActStat> actStatList = actStatRepository.findAllByActCode(act.getCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.ACT_STAT_NOT_FOUND));
        StringBuilder result = new StringBuilder();
        result.append("[ \n").append(" 스탯 변화 발생\n");

        for (ActStat actStat : actStatList) {
            //log.info("관련 스탯은 : "+actStat.getStat().getName());
            if (actStat.getStat().getCode().equals("STAT-007")) {
                //log.info("체력 회복 또는 감소");
                int hp = player.getHp();
                int maxHp = player.getStat().get("STAT-001") * 10;

                hp = Math.min(hp + (bonusPoint * 10), maxHp);
                player = player.toBuilder().hp(hp).build();
                if (bonusPoint > 0) {
                    result.append("< HP >가 " + bonusPoint * 10 + " 회복 되었습니다.\n");
                } else if (bonusPoint < 0) {
                    result.append("< HP >가 " + bonusPoint * 10 + " 감소 했습니다.\n");
                    if (hp + (bonusPoint * 10) <= 0) {
                        result.append(" ] ");
                        return result.toString();
                    }
                }
            } else {
                log.info("스탯 상승 또는 감소");
                String statCode = actStat.getStat().getCode();
                int targetStat = player.getStat().get(statCode);
                int statValue = targetStat + bonusPoint;
                if (statValue < 0) statValue = 0;
                else if (statValue > GameData.MAX_STAT) statValue = GameData.MAX_STAT;

                Map<String, Integer> playerStat = player.getStat();
                playerStat.put(statCode, statValue);

                Stat stat = statRepository.findById(statCode)
                        .orElseThrow(() -> new GameException(GameErrorMessage.STAT_INVALID));

                player.toBuilder().stat(playerStat);
                if (bonusPoint > 0) {
                    result.append("< ").append(stat.getName()).append(" >").append(" 이/가 +").append(bonusPoint)
                            .append(" 증가 했습니다.\n");
                } else {
                    result.append("< ").append(stat.getName()).append(" >").append(" 이/가 ").append(bonusPoint)
                            .append(" 감소 했습니다.\n");
                }
            }
        }

        playerRedisRepository.save(player);
        result.append(" ]");
        return result.toString();
    }

    public ItemRandomGetCommandDto getItem(String storyCode, Player player) {
        //랜덤 아이템 뽑기
        List<Item> itemList = itemRepository.findAllByStoryCode(storyCode)
                .orElse(null);
        if (itemList.isEmpty()) throw new GameException(GameErrorMessage.INVALID_ITEM_REQUEST);

        StringBuilder result = new StringBuilder();
        // 랜덤 객체 생성
        Random random = new Random();
        int index = random.nextInt(itemList.size());
        Item newItem = itemList.get(index);
        player = player.toBuilder()
                .newItemCode(newItem.getCode())
                .build();
        playerRedisRepository.save(player);
        result.append("< ").append(newItem.getName()).append(" > ").append("이/가 나타났다.");

        return ItemRandomGetCommandDto.of(newItem.getCode(), result.toString());
    }

    @Override
    @Transactional
    public PromptGetResponseDto setEnding(EndingCommandDto endingCommandDto) throws GameException {
        ValidateUtil.validate(endingCommandDto);

        Game game = gameRedisRepository.findById(endingCommandDto.gameCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));

        Player player = playerRedisRepository.findById(endingCommandDto.playerCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.PLAYER_NOT_FOUND));

        if(!ValidateUtil.validatePlayer(player.getCode(), game.getPlayerList())) {
            throw new GameException(GameErrorMessage.PLAYER_NOT_FOUND);
        }

        // 엔딩 출력
        List<Prompt> promptList = game.getPromptList();
        if(promptList == null) {
            throw new GameException(GameErrorMessage.PROMPT_INVALID);
        }

        StringBuilder endingPrompt = new StringBuilder();
        for(Prompt prompt : promptList) {
            String name = "";
            if(!prompt.getRole().equals("assistant")) {
                name = playerRedisRepository.findById(prompt.getRole())
                        .orElseThrow(() -> new GameException(GameErrorMessage.PLAYER_NOT_FOUND)).getNickname();
                name += ": ";
            }
            endingPrompt.append(name).append(prompt.getContent()).append("\n");
        }

        String endingMemory = "당신은 아래에서 제시하는 대화 형식을 이야기로 풀어나가야 합니다. \n" +
                "당신은 아래에서 제시하는 대화를 기반으로 미래의 결말을 예측해서 이야기해야 합니다.\n" +
                "당신은 이야기를 끝마쳐야 합니다. 당신은 질문을 할 수 없습니다.\n";

        String ending = chatGptClientUtil.getChatGPTResult(endingMemory, new ArrayList<>(), "### 대화 내용\n" + endingPrompt.toString());
        
        // 플레이어 및 게임 삭제
        for(String playerCode : game.getPlayerList()) {
            playerRedisRepository.delete(playerRedisRepository.findById(playerCode)
                    .orElseThrow(() -> new GameException(GameErrorMessage.PLAYER_NOT_FOUND)));
        }
        gameRedisRepository.delete(game);

        PromptGetResponseDto promptGetResponseDto = PromptGetResponseDto.builder()
                .role("assistant")
                .content(ending)
                .build();
        ValidateUtil.validate(promptGetResponseDto);
        return promptGetResponseDto;
    }
}
