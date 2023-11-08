package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.PromptResultGetCommandDto;
import com.a405.gamept.game.dto.response.PromptResultGetResponseDto;
import com.a405.gamept.game.util.enums.GameErrorMessage;
import com.a405.gamept.game.util.exception.GameException;
import com.a405.gamept.play.entity.Game;
import com.a405.gamept.play.entity.Prompt;
import com.a405.gamept.play.repository.GameRedisRepository;
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
    private final GameRedisRepository gameRedisRepository;

    private final ChatGptClientUtil chatGptClientUtil;

    private final int MAX_PREV_PROMPT_NUMBER = 6;

    @Override
    @Transactional
    public PromptResultGetResponseDto getPrmoptResult(PromptResultGetCommandDto promptResultGetCommandDto) {
        // Game 객체
        Game game = gameRedisRepository.findById(promptResultGetCommandDto.gameCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));

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
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String code = new Random().ints(6, 0, CHARACTERS.length())
                .mapToObj(CHARACTERS::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());

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
