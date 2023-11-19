package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.*;
import com.a405.gamept.game.dto.response.EventGetResponseDto;
import com.a405.gamept.game.dto.response.MonsterGetResponseDto;
import com.a405.gamept.game.dto.response.PromptGetResponseDto;
import com.a405.gamept.game.dto.response.PromptResultGetResponseDto;
import com.a405.gamept.game.entity.Act;
import com.a405.gamept.game.entity.Event;
import com.a405.gamept.game.repository.EmitterRepository;
import com.a405.gamept.game.repository.EventRepository;
import com.a405.gamept.game.util.enums.GameErrorMessage;
import com.a405.gamept.game.util.exception.GameException;
import com.a405.gamept.play.entity.Game;
import com.a405.gamept.play.entity.Player;
import com.a405.gamept.play.entity.Prompt;
import com.a405.gamept.play.repository.GameRedisRepository;
import com.a405.gamept.play.repository.PlayerRedisRepository;
import com.a405.gamept.util.ChatGptClientUtil;
import com.a405.gamept.util.KoreanSummarizerUtil;
import com.a405.gamept.util.ValidateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.lettuce.core.RedisURI.DEFAULT_TIMEOUT;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromptServiceImpl implements PromptService {
    private static final Long DEFAULT_TIMEOUT = 60L * 60 * 1000;
    private final FightService fightService;
    private final EventService eventService;
    private final GameRedisRepository gameRedisRepository;
    private final EventRepository eventRepository;
    private final PlayerRedisRepository playerRedisRepository;
    private final EmitterRepository emitterRepository;
    private final ChatGptClientUtil chatGptClientUtil;
    private final int MAX_PREV_PROMPT_NUMBER = 6;

    @Override
    @Transactional(readOnly = true)
    public PromptGetResponseDto setUserPrompt(PromptResultGetCommandDto promptResultGetCommandDto) throws GameException {
        log.info("setUserPrompt() 호출");
        ValidateUtil.validate(promptResultGetCommandDto);
        Game game = gameRedisRepository.findById(promptResultGetCommandDto.gameCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));
        Player player = playerRedisRepository.findById(promptResultGetCommandDto.playerCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.PLAYER_NOT_FOUND));


        if (!ValidateUtil.validatePlayer(player.getCode(), game.getPlayerList())) {
            throw new GameException(GameErrorMessage.PLAYER_NOT_FOUND);
        }

        PromptGetResponseDto promptGetResponseDto = PromptGetResponseDto.builder()
                .role(player.getCode())
                .content("플레이어 '" + player.getNickname() + "' : " + promptResultGetCommandDto.prompt())
                .build();  // 클라이언트에 보낼 플레이어 입력 프롬프트
        ValidateUtil.validate(promptGetResponseDto);

        log.info("setUserPrompt() 종료");
        return promptGetResponseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public PromptResultGetCommandDto getChatGPTPrompt(PromptResultGetCommandDto promptResultGetCommandDto) {
        log.info("getChatGPTPrompt() 호출");
        Game game = gameRedisRepository.findById(promptResultGetCommandDto.gameCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));
        Player player = playerRedisRepository.findById(promptResultGetCommandDto.playerCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.PLAYER_NOT_FOUND));

        /** 이벤트 프롬프트 자동 삽입 여부 확인 **/
        String promptInput = "플레이어 '" + player.getNickname() + "' : " + promptResultGetCommandDto.prompt();
        // promptInput = eventService.pickAtRandomEvent(game, promptInput);

        if (30 <= game.getTurn()) {  // 마왕 등장 처리 로직
            promptInput += "[전투] 갑자기... 마왕 '우서기우스'라는 자가 나에게 전투를 걸어왔다. [전투]를 앞에 붙여 얘기하세요.";
        } else if (game.getEventCnt() < 10) {
            promptInput += insertEventPrompt(game.getStoryCode(), game.getEventRate());
        }

        promptResultGetCommandDto = promptResultGetCommandDto.toBuilder().prompt(promptInput).build();
        ValidateUtil.validate(promptResultGetCommandDto);

        log.info("getChatGPTPrompt() 종료");
        return promptResultGetCommandDto;
    }

    @Override
    @Transactional
    public PromptResultGetResponseDto getPrmoptResult(PromptResultGetCommandDto promptResultGetCommandDto, String responsePrompt) {
        log.info("getPromptResult() 호출");
        Game game = gameRedisRepository.findById(promptResultGetCommandDto.gameCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));
        // Player 객체
        Player player = playerRedisRepository.findById(promptResultGetCommandDto.playerCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.PLAYER_NOT_FOUND));

        List<Prompt> promptList = game.getPromptList();
        String memory = game.getMemory();

        promptList.add(Prompt.builder()
                .role(player.getCode())
                .content(promptResultGetCommandDto.prompt())
                .build());  // User Redis 추가
        promptList.add(Prompt.builder()
                .role("assistant")
                .content(responsePrompt)
                .build());  // ChatGPT 응답 Redis 추가

        // game = savePromptLog(game, "user", promptResultGetCommandDto.prompt());
        // game = savePromptLog(game, "assistant", responsePrompt);
        while (MAX_PREV_PROMPT_NUMBER <= promptList.size()) {  // 요약
            memory = KoreanSummarizerUtil.summarize(
                    game.getMemory() + '\n' + promptList.remove(0).getContent()
            );
        }

        // EventCommandDto eventCommandDto = eventService.checkEventInPrompt(game, responsePrompt);
        Event event = null;
        if (game.getEventCnt() < 10/*EVENT_MAX_COUNT*/ && game.getTurn() < 30) {  // 대화 30턴 미만, 이벤트 발생 횟수 10번 미만일 경우
            // 스토리 코드에 따른 이벤트 리스트
            List<Event> eventList = eventRepository.findAllByStoryCode(game.getStoryCode())
                    .orElseThrow(() -> new GameException(GameErrorMessage.EVENT_NOT_FOUND));

            // 텍스트에서 가장 마지막에 등장한 이벤트의 인덱스
            int eventIndex = eventService.findLastEventInText(eventList, responsePrompt);
            event = (0 <= eventIndex) ? eventList.get(eventIndex) : null;
        }

        double eventRate = game.getEventRate();
        int eventCnt = game.getEventCnt();
        if (event == null) {  // 이벤트 발생하지 않았을 경우
            eventRate += 0.05;  // 이벤트 발생 확률 5% 상승
        } else {
            eventRate = 0.00;  // 이벤트 발생 확률 0%로 초기화
            eventCnt += 1;  // 이벤트 발생 횟수 + 1
        }

        MonsterGetResponseDto monsterGetResponseDto = null;
        if (game.getTurn() < 30  // 이벤트가 마왕 처치일 경우
                || (event != null && event.getCode().equals("EV-001"))) {  // 이벤트가 전투일 경우
            monsterGetResponseDto = fightService.getMonster(MonsterSetCommandDto.builder()
                    .gameCode(game.getCode())
                    .playerCode(player.getCode())
                    .build()
            );   // 몬스터 생성
        }

        gameRedisRepository.save(game.toBuilder()
                .eventCnt(eventCnt)
                .eventRate(eventRate)
                .promptList(game.getPromptList())
                .turn(game.getTurn() + 1)
                .memory(memory)
                .fightingEnemyCode(monsterGetResponseDto != null ? monsterGetResponseDto.code() : null)
                .build());

        PromptResultGetResponseDto promptResultGetResponseDto = PromptResultGetResponseDto.builder()
                .gameCode(game.getCode())
                // .prompt(responsePrompt)
                .event((event != null)? EventGetResponseDto.from(event) : null)
                .monster(monsterGetResponseDto)
                .build();
        ValidateUtil.validate(promptResultGetResponseDto);

        log.info("getPromptResult() 종료");
        return promptResultGetResponseDto;
    }

    @Override
    public List<PromptGetResponseDto> getPromptList(PromptListGetCommandDto promptListGetCommandDto) {
        Game game = gameRedisRepository.findById(promptListGetCommandDto.gameCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));
        Player player = playerRedisRepository.findById(promptListGetCommandDto.playerCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.PLAYER_NOT_FOUND));

        // 플레이어가 방에 존재하지 않을 경우
        if (!ValidateUtil.validatePlayer(player.getCode(), game.getPlayerList())) {
            throw new GameException(GameErrorMessage.PLAYER_NOT_FOUND);
        }

        List<Prompt> promptList = game.getPromptList();
        if (promptList == null) {
            throw new GameException(GameErrorMessage.PROMPT_INVALID);
        }

        List<PromptGetResponseDto> promptGetResponseDtoList = new ArrayList<>();
        PromptGetResponseDto promptGetResponseDto;
        for (Prompt prompt : promptList) {
            promptGetResponseDto = PromptGetResponseDto.from(prompt);
            ValidateUtil.validate(promptGetResponseDto);

            promptGetResponseDtoList.add(promptGetResponseDto);
        }

        return promptGetResponseDtoList;
    }

    /**
     * 클라이언트가 구독을 위해 호출하는 메서드
     *
     * @param gameCode
     * @return
     */
    @Override
    @Transactional
    public SseEmitter subscribeEmitter(String gameCode) {
        SseEmitter emitter = createEmitter(gameCode);

        sendToClient(gameCode, "EventStream Created. [gameCode=" + gameCode + "]");
        return emitter;
    }

    private void sendToClient(String gameCode, Object data) {
        SseEmitter emitter = emitterRepository.get(gameCode);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().id(gameCode).name("sse").data(data));
            } catch (IOException exception) {
                emitterRepository.deleteById(gameCode);
                emitter.completeWithError(exception);
            }
        }
    }

    @Override
    public String sendPrompt(String gameCode, String inputPrompt) throws JsonProcessingException {
        Game game = gameRedisRepository.findById(gameCode)
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));
        SseEmitter emitter = emitterRepository.get(gameCode);
        String outputPrompt = chatGptClientUtil.enterPromptForSse(emitter, inputPrompt, game.getSettingPrompt(), game.getMemory(), game.getPromptList());
        log.info("ChatGPT Result: " + outputPrompt);

        return outputPrompt;
    }

    private SseEmitter createEmitter(String gameCode) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(gameCode, emitter);

        emitter.onCompletion(() -> emitterRepository.deleteById(gameCode));
        emitter.onTimeout(() -> emitterRepository.deleteById(gameCode));

        return emitter;
    }

    /**
     * savePromptLog <br/><br/>
     * <p>
     * 지금까지 주고 받았던 프롬프트 로그를 저장.
     *
     * @param game
     * @param role
     * @param content
     */
    private Game savePromptLog(Game game, String role, String content) {
        // 메모리 업데이트
        game = updatePromptMemory(game);
        List<Prompt> promptList = game.getPromptList();

        // 새로운 프롬프트 로그 생성
        Prompt newPrompt = Prompt.builder()
                .role(role)
                .content(content)
                .build();
        promptList.add(newPrompt);

        // 새로운 프롬프트 로그가 추가된 game 객체 반환
        return game.toBuilder()
                .promptList(promptList)
                .build();
    }

    private Game updatePromptMemory(Game game) {
        List<Prompt> promptList = game.getPromptList();

        // 요약해서 메모리에 저장.
        if (promptList.size() >= MAX_PREV_PROMPT_NUMBER) {
            StringBuilder newMemorySB = new StringBuilder();

            Prompt prevPrompt = promptList.remove(0);
            newMemorySB.append(game.getMemory()).append('\n').append(prevPrompt.getContent());

            // 텍스트 요약 라이브러리
            String newMemory = KoreanSummarizerUtil.summarize(newMemorySB.toString());
            game = game.toBuilder()
                    .memory(newMemory)
                    .promptList(promptList)
                    .build();
        }

        return game;
    }

    private String insertEventPrompt(String storyCode, double eventRate) throws GameException {
        if (Math.random() <= eventRate) {  // 이벤트 발생해야할 경우
            List<Event> eventList = eventRepository.findAllByStoryCode(storyCode)
                    .orElseThrow(() -> new GameException(GameErrorMessage.EVENT_NOT_FOUND));

            double sum = 0, random;
            for (Event event : eventList) {
                sum += event.getWeight();  // 가중치 계산
            }

            random = Math.floor(Math.random() * sum);
            sum = 0;
            for (Event event : eventList) {
                sum += event.getWeight();
                if (random < sum) {  // 확률에 해당할 경우
                    return "\n" + event.getPrompt()
                            + "\n[" + event.getName() + "]를 앞에 붙여 얘기하세요.";
                }
            }
        }

        return "";
    }

}
