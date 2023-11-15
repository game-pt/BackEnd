package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.PromptResultGetCommandDto;
import com.a405.gamept.game.dto.response.PromptResultGetResponseDto;
import com.a405.gamept.game.entity.Event;
import com.a405.gamept.play.entity.Game;

import java.util.List;

public interface EventService {

    List<Event> findAllEventByStoryCode(String storyCode);
    PromptResultGetCommandDto pickAtRandomEvent(PromptResultGetCommandDto promptResultGetCommandDto, Game game);
    PromptResultGetResponseDto checkEventInPrompt(PromptResultGetCommandDto promptResultGetCommandDto, Game game);

}
