package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.GetPromptResultCommandDto;
import com.a405.gamept.game.dto.response.GetPromptResultResponseDto;
import com.a405.gamept.game.entity.Event;
import com.a405.gamept.util.ChatGptClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PromptServiceImpl implements PromptService {

    private final EventService eventService;

    private final ChatGptClientUtil chatGptClientUtil;

    @Override
    public GetPromptResultResponseDto getPrmoptResult(GetPromptResultCommandDto getPromptResultCommandDto) {
        // 이벤트 트리거 프롬프트 추가
        GetPromptResultCommandDto getPromptResultCommandDtoForEventPrompt = eventService.pickAtRandomEvent(getPromptResultCommandDto);
        
        // ChatGPT에 프롬프트 전송
        String promptResult = chatGptClientUtil.enterPrompt(getPromptResultCommandDto.prompt());

        // 최종적으로 응답에 이벤트를 추가하여 클라이언트에게 반환할 형태로 ResponseDto를 구성
        GetPromptResultCommandDto getPromptResultCommandDtoForCheck = GetPromptResultCommandDto.from(getPromptResultCommandDto, promptResult);
        GetPromptResultResponseDto getPromptResultResponseDto = eventService.checkEventInPrompt(getPromptResultCommandDtoForCheck);

        return getPromptResultResponseDto;
    }

}
