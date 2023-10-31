package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.request.FindEventByStoryCodeRequestDto;
import com.a405.gamept.game.entity.Event;

import java.util.List;

public interface EventService {

    List<Event> findAllEventByStoryCode(FindEventByStoryCodeRequestDto storyDto);
    Event pickAtRandomEventByStoryCode(FindEventByStoryCodeRequestDto storyDto);

}
