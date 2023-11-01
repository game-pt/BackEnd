package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.GetPromptResultCommandDto;
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
    public GetPromptResultCommandDto getPrmoptResult(GetPromptResultCommandDto getPromptResultCommandDto) {
        GetPromptResultCommandDto getPromptResultCommandDtoForEventPrompt = eventService.pickAtRandomEvent(getPromptResultCommandDto);
        String promptResult = chatGptClientUtil.enterPrompt(getPromptResultCommandDto.prompt());
        GetPromptResultCommandDto getPromptResultCommandDtoForCheck = GetPromptResultCommandDto.from(getPromptResultCommandDto, promptResult);

        Event occuredEvent = eventService.checkEventInPrompt(getPromptResultCommandDtoForCheck);
        if (occuredEvent == null) {
            return getPromptResultCommandDto;
        }

        return
    }

}
