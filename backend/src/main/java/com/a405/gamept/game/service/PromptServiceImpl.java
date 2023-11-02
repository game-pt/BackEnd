package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.GetPromptResultCommandDto;
import com.a405.gamept.game.dto.response.GetPromptResultResponseDto;
import com.a405.gamept.game.util.enums.GameErrorMessage;
import com.a405.gamept.game.util.exception.GameException;
import com.a405.gamept.play.entity.Game;
import com.a405.gamept.play.repository.GameRedisRepository;
import com.a405.gamept.util.ChatGptClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PromptServiceImpl implements PromptService {

    private final EventService eventService;
    private final GameRedisRepository gameRedisRepository;

    private final ChatGptClientUtil chatGptClientUtil;

    @Override
    @Transactional
    public GetPromptResultResponseDto getPrmoptResult(GetPromptResultCommandDto getPromptResultCommandDto) {
        // 이벤트 트리거 프롬프트 추가
        GetPromptResultCommandDto getPromptResultCommandDtoForEventPrompt = eventService.pickAtRandomEvent(getPromptResultCommandDto);

        // 턴 횟수 증가
        Game game = gameRedisRepository.findById(getPromptResultCommandDto.code())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));
        gameRedisRepository.save(game.toBuilder()
                .turn(game.getTurn() + 1)
                .build());

        // ChatGPT에 프롬프트 전송
        String promptResult = chatGptClientUtil.enterPrompt(getPromptResultCommandDtoForEventPrompt.prompt());

        // 최종적으로 응답에 이벤트를 추가하여 클라이언트에게 반환할 형태로 ResponseDto를 구성
        GetPromptResultCommandDto getPromptResultCommandDtoForCheck = GetPromptResultCommandDto.from(getPromptResultCommandDto, promptResult);
        GetPromptResultResponseDto getPromptResultResponseDto = eventService.checkEventInPrompt(getPromptResultCommandDtoForCheck);

        return getPromptResultResponseDto;
    }

}
