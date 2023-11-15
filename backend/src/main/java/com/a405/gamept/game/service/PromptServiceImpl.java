package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.*;
import com.a405.gamept.game.dto.response.MonsterGetResponseDto;
import com.a405.gamept.game.dto.response.PromptGetResponseDto;
import com.a405.gamept.game.dto.response.PromptResultGetResponseDto;
import com.a405.gamept.game.entity.Act;
import com.a405.gamept.game.entity.Event;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromptServiceImpl implements PromptService {
    private final FightService fightService;
    private final GameRedisRepository gameRedisRepository;
    private final EventRepository eventRepository;
    private final PlayerRedisRepository playerRedisRepository;
    private final ChatGptClientUtil chatGptClientUtil;
    private final int MAX_PREV_PROMPT_NUMBER = 6;

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
                .content(promptResultGetCommandDto.prompt())
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

        /** 이벤트 프롬프트 자동 삽입 여부 확인 **/
        double eventRate = game.getEventRate();
        String promptInput = promptResultGetCommandDto.prompt()
                + insertEventPrompt(game.getStoryCode(), eventRate);

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

        Event selectedEvent = findEvent(game.getStoryCode(), responsePrompt);
        EventCommandDto eventCommandDto = null;
        if (selectedEvent == null) {  // 이벤트 발생하지 않았을 경우
            eventRate += 0.05;  // 이벤트 발생 확률 5% 상승
        } else {
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


    // 이벤트 트리거 프롬프트 추가
    // PromptResultGetCommandDto promptResultCommandDtoForEvent = eventService.pickAtRandomEvent(promptResultGetCommandDto, game);
        /*
        // ChatGPT에 프롬프트 전송
        String promptOutput = chatGptClientUtil.enterPrompt(promptInput, game.getMemory(), game.getPromptList());

        // 턴 횟수 증가
        game = increaseTurn(game);
        // 플레이어의 프롬프트 로그 저장
        game = savePromptLog(game, "user", promptInput);
        // ChatGPT의 프롬프트 로그 저장
        game = savePromptLog(game, "assistant", promptOutput);
        // 최종적으로 game 데이터를 Redis에 저장.
        gameRedisRepository.save(game);

        // 최종적으로 응답에 이벤트를 추가하여 클라이언트에게 반환할 형태로 ResponseDto를 구성
        PromptResultGetCommandDto promptResultGetCommandDtoForCheck = PromptResultGetCommandDto.from(promptResultGetCommandDto, promptOutput);
        PromptResultGetResponseDto promptResultGetResponseDto = eventService.checkEventInPrompt(promptResultGetCommandDtoForCheck, game);
        // ValidateUtil.validate(promptResultGetResponseDto);

        // 전투 로직 찾기 : 유영 추가
        if(promptResultGetResponseDto.event() != null && promptResultGetResponseDto.event().eventCode().equals("EV-001")) {  // 이벤트가 전투일 경우
            fightService.setMonster(MonsterSetCommandDto.builder()
                    .gameCode(game.getCode())
                    .playerCode(player.getCode())
                    .build()
            );   // 몬스터 생성

            MonsterGetCommandDto monsterGetCommandDto = MonsterGetCommandDto.builder()
                    .gameCode(game.getCode())
                    .build();  // 적을 가져오는 데에 필요한 DTO 생성 
            promptResultGetResponseDto = PromptResultGetResponseDto.of(promptResultGetResponseDto, fightService.getMonster(monsterGetCommandDto));
        }
        ValidateUtil.validate(promptResultGetResponseDto);
        */

    // return null;

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
            if (prompt.contains("[" + event.getName())) {
                return event;
            }
        }

        return null;
    }

}
