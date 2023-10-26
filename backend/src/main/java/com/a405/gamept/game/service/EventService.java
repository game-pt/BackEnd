package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.FindEventByStoryCodeReq;
import com.a405.gamept.game.entity.Event;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EventService {

    List<Event> findAllEventByStoryCode(FindEventByStoryCodeReq storyDto);
    Event pickAtRandomEventByStoryCode(FindEventByStoryCodeReq storyDto);

}
