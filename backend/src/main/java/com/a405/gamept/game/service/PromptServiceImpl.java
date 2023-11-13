package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.MonsterGetCommandDto;
import com.a405.gamept.game.dto.command.MonsterSetCommandDto;
import com.a405.gamept.game.dto.command.PromptResultGetCommandDto;
import com.a405.gamept.game.dto.response.PromptResultGetResponseDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromptServiceImpl implements PromptService {

    private final EventService eventService;
    private final FightService fightService;
    private final GameRedisRepository gameRedisRepository;
    private final PlayerRedisRepository playerRedisRepository;

    private final ChatGptClientUtil chatGptClientUtil;

    private final int MAX_PREV_PROMPT_NUMBER = 6;

    @Override
    @Transactional
    public PromptResultGetResponseDto getPrmoptResult(PromptResultGetCommandDto promptResultGetCommandDto) {
        // Game 객체
        Game game = gameRedisRepository.findById(promptResultGetCommandDto.gameCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));
        // Player 객체
        Player player = playerRedisRepository.findById(promptResultGetCommandDto.playerCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.PLAYER_NOT_FOUND));

        // 플레이어가 방에 존재하지 않을 경우
        if(!ValidateUtil.validatePlayer(player.getCode(), game.getPlayerList())) {
            throw new GameException(GameErrorMessage.PLAYER_NOT_FOUND);
        }

        // 이벤트 트리거 프롬프트 추가
        PromptResultGetCommandDto promptResultCommandDtoForEvent = eventService.pickAtRandomEvent(promptResultGetCommandDto, game);

        // ChatGPT에 프롬프트 전송
        String promptInput = promptResultCommandDtoForEvent.prompt();
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

        return promptResultGetResponseDto;
    }

    /**
     * savePromptLog <br/><br/>
     *
     * 지금까지 주고 받았던 프롬프트 로그를 저장.
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

    private Game increaseTurn(Game game) {
        return gameRedisRepository.save(game.toBuilder()
                .turn(game.getTurn() + 1)
                .build());
    }

}
