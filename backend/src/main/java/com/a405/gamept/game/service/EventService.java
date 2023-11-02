package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.FindEventByStoryCodeCommandDto;
import com.a405.gamept.game.dto.command.GetPromptResultCommandDto;
import com.a405.gamept.game.dto.response.GetPromptResultResponseDto;
import com.a405.gamept.game.entity.Event;

import java.util.List;

public interface EventService {

    List<Event> findAllEventByStoryCode(String storyCode);
    GetPromptResultCommandDto pickAtRandomEvent(GetPromptResultCommandDto getPromptResultCommandDto);
    GetPromptResultResponseDto checkEventInPrompt(GetPromptResultCommandDto getPromptResultCommandDto);

}
