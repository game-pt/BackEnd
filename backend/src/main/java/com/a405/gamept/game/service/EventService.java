package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.FindEventByStoryCodeRequest;
import com.a405.gamept.game.entity.Event;

import java.util.List;

public interface EventService {

    List<Event> findAllEventByStoryCode(FindEventByStoryCodeRequest storyDto);
    Event pickAtRandomEventByStoryCode(FindEventByStoryCodeRequest storyDto);

}
