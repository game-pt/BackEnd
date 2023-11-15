package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.*;
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
    private final FightService fightService;
    private final GameRedisRepository gameRedisRepository;
    private final EventRepository eventRepository;
    private final PlayerRedisRepository playerRedisRepository;
    private final EmitterRepository emitterRepository;
    private final ChatGptClientUtil chatGptClientUtil;
    private final int MAX_PREV_PROMPT_NUMBER = 6;
    private static final Long DEFAULT_TIMEOUT = 60L * 60 * 1000;

    @Override
    @Transactional(readOnly = true)
    public PromptGetResponseDto setUserPrompt(PromptResultGetCommandDto promptResultGetCommandDto) throws GameException {
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
                .content(promptResultGetCommandDto.prompt()
                        .replace("나는", player.getNickname() + "은(는)")
                        .replace("난 ", player.getNickname() + "은(는) ")
                        .replace("내가", player.getNickname() + "이(가)")
                        .replace("나의", player.getNickname() + "의")
                        .replace("나도", player.getNickname() + "도")
                        .replace("나랑", player.getNickname() + "랑")
                        .replace("나와", player.getNickname() + "와(과)")
                        .replace("나만", player.getNickname() + "만")
                )
                .build();  // 클라이언트에 보낼 플레이어 입력 프롬프트
        ValidateUtil.validate(promptGetResponseDto);

        return promptGetResponseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public PromptGetResponseDto getChatGPTPrompt(PromptResultGetCommandDto promptResultGetCommandDto) {
        log.info("getChatGPTPrompt 호출");
        Game game = gameRedisRepository.findById(promptResultGetCommandDto.gameCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));
        Player player = playerRedisRepository.findById(promptResultGetCommandDto.playerCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.PLAYER_NOT_FOUND));

        /** 이벤트 프롬프트 자동 삽입 여부 확인 **/
        double eventRate = game.getEventRate();
        String promptInput = promptResultGetCommandDto.prompt()
                .replace("나는", "플레이어인 '" + player.getNickname() + "'은(는)")
                .replace("난 ", "플레이어인 '" + player.getNickname() + "'은(는) ")
                .replace("내가", "플레이어인 '" + player.getNickname() + "'이(가)")
                .replace("나도", "플레이어인 '" + player.getNickname() + "'도")
                .replace("나랑", "플레이어인 '" + player.getNickname() + "'랑")
                .replace("나와", "플레이어인 '" + player.getNickname() + "'와(과)")
                .replace("나만", "플레이어인 '" + player.getNickname() + "'만");
        if(30 <= game.getTurn()) {
            promptInput +=  eventRepository.findById("EV-007")
                    .orElseThrow(() -> new GameException(GameErrorMessage.EVENT_NOT_FOUND)).getPrompt();
        } else {
            promptInput +=  insertEventPrompt(game.getStoryCode(), eventRate);
        }
        log.info("최종 유저 프롬프트: " + promptInput);

        /** ChatGPT에 프롬프트 전송 **/
        List<Prompt> promptList = game.getPromptList();
        if (promptList == null) {
            throw new GameException(GameErrorMessage.PROMPT_INVALID);
        }
        String responsePrompt = chatGptClientUtil.getChatGPTResult(game.getMemory(), promptList, promptInput);

        PromptGetResponseDto promptGetResponseDto = PromptGetResponseDto
                .builder()
                .role("assistant")
                .content(responsePrompt)
                .build();  // 클라이언트에 보낼 ChatGPT 입력 프롬프트

        ValidateUtil.validate(promptGetResponseDto);
        return promptGetResponseDto;
    }

    @Override
    @Transactional
    public PromptResultGetResponseDto getPrmoptResult(PromptResultGetCommandDto promptResultGetCommandDto, String responsePrompt) {
        log.info("getPrmoptResult 호출");
        Game game = gameRedisRepository.findById(promptResultGetCommandDto.gameCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));
        // Player 객체
        Player player = playerRedisRepository.findById(promptResultGetCommandDto.playerCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.PLAYER_NOT_FOUND));

        double eventRate = game.getEventRate();
        int eventCnt = game.getEventCnt();
        List<Prompt> promptList = game.getPromptList();
        promptList.add(Prompt.builder()
                .role(player.getCode())
                .content(promptResultGetCommandDto.prompt())
                .build());  // 플레이어 응답 Redis 추가
        promptList.add(Prompt.builder()
                .role("assistant")
                .content(responsePrompt)
                .build());  // ChatGPT 응답 Redis 추가

        while(10 < promptList.size()) {
            promptList.remove(0);
        }


        Event selectedEvent = null;
        if(game.getEventCnt() < 10 && game.getTurn() < 30) {  // 대화 30턴 미만, 이벤트 발생 횟수 10번 미만일 경우
            selectedEvent = findEvent(game.getStoryCode(), responsePrompt);  // 이벤트 찾기
        } else if(30 <= game.getTurn()) {  // 대화가 30턴을 넘었을 경우
            selectedEvent = eventRepository.findById("EV-007")  // 마왕 발생
                    .orElseThrow(() -> new GameException(GameErrorMessage.EVENT_NOT_FOUND));
        }

        EventCommandDto eventCommandDto = null;
        if (selectedEvent == null) {  // 이벤트 발생하지 않았을 경우
            eventRate += 0.05;  // 이벤트 발생 확률 5% 상승
        } else {
            log.info(selectedEvent.toString() + " 이벤트 발생");
            eventRate = 0.00;  // 이벤트 발생 확률 0%로 초기화
            eventCnt += 1;  // 이벤트 발생 횟수 + 1

            List<ActCommandDto> actCommandDtoList = new ArrayList<>();
            for (Act act : selectedEvent.getActList()) {
                actCommandDtoList.add(ActCommandDto.from(act));
            }

            MonsterGetResponseDto monsterGetResponseDto = null;
            if (selectedEvent.getCode().equals("EV-001")) {  // 이벤트가 전투일 경우
                monsterGetResponseDto = fightService.getMonster(MonsterSetCommandDto.builder()
                        .gameCode(game.getCode())
                        .playerCode(player.getCode())
                        .build()
                );   // 몬스터 생성
            } else if(selectedEvent.getCode().equals("EV-007")) {  // 이벤트가 마왕 처치일 경우
                monsterGetResponseDto = fightService.getMonster(MonsterSetCommandDto.builder()
                        .gameCode(game.getCode())
                        .playerCode(player.getCode())
                        .build()
                );   // 몬스터 생성
            }
            eventCommandDto = EventCommandDto.from(selectedEvent, actCommandDtoList, monsterGetResponseDto);
        }

        gameRedisRepository.save(game.toBuilder()
                .promptList(promptList)
                .eventRate(eventRate)
                .eventCnt(eventCnt)
                .turn(game.getTurn() + 1)
                .fightingEnemyCode((eventCommandDto != null && eventCommandDto.monster() != null) ? eventCommandDto.monster().code() : null)
                .build());

        PromptResultGetResponseDto promptResultGetResponseDto = PromptResultGetResponseDto.builder()
                .gameCode(game.getCode())
                // .prompt(responsePrompt)
                .event(eventCommandDto)
                .monster((eventCommandDto != null && eventCommandDto.monster() != null) ? eventCommandDto.monster() : null)
                .build();
        ValidateUtil.validate(promptResultGetResponseDto);
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
    public SseEmitter subscribeEmitter(String gameCode) throws JsonProcessingException {
//        ObjectMapper mapper = new ObjectMapper();
//
//        Game game = gameRedisRepository.findById(gameCode)
//                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));
//        if (game.getMappedEmitter() == null) {
//            SseEmitter emitter = createEmitter(gameCode);
//            String mappedEmitter = mapper.writeValueAsString(emitter);
//            game = game.toBuilder()
//                    .mappedEmitter(mappedEmitter)
//                    .build();
//            gameRedisRepository.save(game);
//        }
//
//        sendToClient(gameCode, "EventStream Created. [gameCode=" + gameCode + "]");
//        SseEmitter emitter = mapper.readValue(game.getMappedEmitter(), SseEmitter.class);
//        System.out.println("emitter.hashCode(): " + emitter.hashCode());
//
//        try {
//            emitter.send("data");
//            emitter.complete();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return emitter;

        SseEmitter emitter = createEmitter(gameCode);

        sendToClient(gameCode, "EventStream Created. [gameCode=" + gameCode + "]");
        return emitter;
    }

    private void sendToClient(String gameCode, Object data) throws JsonProcessingException {
//        ObjectMapper mapper = new ObjectMapper();
//
//        Game game = gameRedisRepository.findById(gameCode)
//                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));
//        SseEmitter emitter = mapper.readValue(game.getMappedEmitter(), SseEmitter.class);
//        if (emitter != null) {
//            try {
////                emitter.send(SseEmitter.event().id(gameCode).name("sse").data(data));
//                emitter.send(SseEmitter.event().data(data));
//            } catch (IOException exception) {
//                log.error(exception.getMessage());
//            }
//        }

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
    public void sendPrompt(String gameCode, Object data) throws JsonProcessingException {
        sendToClient(gameCode, data);
    }

    private SseEmitter createEmitter(String gameCode) {
//        ObjectMapper mapper = new ObjectMapper();
//
//        Game game = gameRedisRepository.findById(gameCode)
//                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));
//
//        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
//
//        emitter.onCompletion(() -> gameRedisRepository.save(game.toBuilder()
//                .mappedEmitter(null)
//                .build()));
//        emitter.onTimeout(() -> gameRedisRepository.save(game.toBuilder()
//                .mappedEmitter(null)
//                .build()));
//
//        return emitter;

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

        // 랜덤 코드 발생
        String code = ValidateUtil.getRandomUID();

        // 새로운 프롬프트 로그 생성
        Prompt newPrompt = Prompt.builder()
                .code(game.getCode() + "-" + code)
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
            try {
                Event event = eventList.get((int) Math.floor(Math.random() * eventList.size()));
                return "\n" + event.getPrompt();
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new GameException(GameErrorMessage.EVENT_NOT_FOUND);
            }
        }

        return "";
    }

    private Event findEvent(String storyCode, String prompt) {
        if (!prompt.contains("[")) return null;
        List<Event> eventList = eventRepository.findAllByStoryCode(storyCode)
                .orElseThrow(() -> new GameException(GameErrorMessage.EVENT_NOT_FOUND));

        for (Event event : eventList) {
            if (prompt.contains("[" + event.getName()) || prompt.contains(event.getName() + "]")) {
                return event;
            }
        }

        return null;
    }

}
