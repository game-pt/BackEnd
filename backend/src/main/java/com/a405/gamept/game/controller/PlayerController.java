package com.a405.gamept.game.controller;

import com.a405.gamept.game.dto.command.JobGetCommandDto;
import com.a405.gamept.game.dto.command.PlayerSetCommandDto;
import com.a405.gamept.game.dto.command.RaceGetCommandDto;
import com.a405.gamept.game.dto.request.JobGetRequestDto;
import com.a405.gamept.game.dto.request.PlayerSetRequestDto;
import com.a405.gamept.game.dto.request.RaceGetRequestDto;
import com.a405.gamept.game.service.PlayerService;
import com.a405.gamept.game.util.exception.GameException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("player")
public class PlayerController {
    private final SimpMessagingTemplate webSocket;
    private final PlayerService playerService;

    @Autowired
    public PlayerController(SimpMessagingTemplate webSocket, PlayerService playerService) {
        this.webSocket = webSocket;
        this.playerService = playerService;
    }

    @GetMapping("race")
    public ResponseEntity<?> getRaceList(@Valid RaceGetRequestDto raceGetRequestDto) throws GameException {
        return ResponseEntity.ok(playerService.getRaceList(RaceGetCommandDto.from(raceGetRequestDto)));
    }

    @GetMapping("job")
    public ResponseEntity<?> getJobList(@Valid JobGetRequestDto jobGetRequestDto) throws GameException {
        return ResponseEntity.ok(playerService.getJobList(JobGetCommandDto.from(jobGetRequestDto)));
    }

    @PostMapping
    public ResponseEntity<?> setPlayer(@Valid @RequestBody PlayerSetRequestDto playerSetRequestDto) throws GameException {
        return ResponseEntity.ok(playerService.setPlayer(PlayerSetCommandDto.from(playerSetRequestDto)));
    }
}
