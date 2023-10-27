package com.a405.gamept.game.controller;

import com.a405.gamept.game.dto.FindEventByStoryCodeRequest;
import com.a405.gamept.game.dto.FindEventByStoryCodeResponse;
import com.a405.gamept.game.entity.Event;
import com.a405.gamept.game.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/api/event/random")
    public ResponseEntity<FindEventByStoryCodeResponse> pickAtRandomEvent(FindEventByStoryCodeRequest eventRequest) {
        Event event = eventService.pickAtRandomEventByStoryCode(eventRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(FindEventByStoryCodeResponse.builder()
                        .code(event.getCode())
                        .name(event.getName())
                        .prompt(event.getPrompt())
                        .item_yn(event.getItemYn())
                        .weight(event.getWeight())
                        .build());
    }

}
