package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.GetPromptResultCommandDto;
import com.a405.gamept.game.dto.request.FindEventByStoryCodeRequestDto;
import com.a405.gamept.game.entity.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {

    List<Event> findAllEventByStoryCode(FindEventByStoryCodeRequestDto storyDto);
    Event pickAtRandomEventByStoryCode(FindEventByStoryCodeRequestDto storyDto);
    GetPromptResultCommandDto checkEventInPrompt(GetPromptResultCommandDto getPromptResultCommandDto);

}
