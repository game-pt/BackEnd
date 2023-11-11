package com.a405.gamept.game.controller;

import com.a405.gamept.game.dto.command.*;
import com.a405.gamept.game.dto.request.JobGetRequestDto;
import com.a405.gamept.game.dto.request.PlayerGetRequestDto;
import com.a405.gamept.game.dto.request.PlayerSetRequestDto;
import com.a405.gamept.game.dto.request.RaceGetRequestDto;
import com.a405.gamept.game.dto.response.PlayerStatGetResponseDto;
import com.a405.gamept.game.service.PlayerService;
import com.a405.gamept.game.util.exception.GameException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("player")
@Slf4j
public class PlayerController {
    private final PlayerService playerService;
    private final SimpMessagingTemplate webSocket;

    @Autowired
    public PlayerController(PlayerService playerService, SimpMessagingTemplate webSocket) {
        this.playerService = playerService;
        this.webSocket = webSocket;
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

    @GetMapping
    public ResponseEntity<?> getPlayer(@Valid PlayerGetRequestDto playerGetRequestDto) throws GameException {
        return ResponseEntity.ok(playerService.getPlayer(PlayerGetCommandDto.from(playerGetRequestDto)));
    }

    @MessageMapping("/stat/{gameCode}/{playerCode}")
    public void getPlayerStat(@Valid @DestinationVariable String gameCode, @Valid @DestinationVariable String playerCode) {
        PlayerStatGetResponseDto playerStatGetResponseDto = playerService.getPlayerStat(PlayerStatGetCommandDto.of(gameCode, playerCode));
        webSocket.convertAndSendToUser(playerCode, "/stat", playerStatGetResponseDto);
    }

    @MessageMapping("/stat-up/{gameCode}/{playerCode}/{statCode}")
    public void addOnePlayerStat(
            @Valid @DestinationVariable String gameCode,
            @Valid @DestinationVariable String playerCode,
            @Valid @DestinationVariable String statCode) {
        PlayerStatGetResponseDto playerStatGetResponseDto = playerService.addPlayerStat(PlayerStatUpdateCommandDto.of(gameCode, playerCode, statCode, 1));
        webSocket.convertAndSendToUser(playerCode, "/stat", playerStatGetResponseDto);
    }
}
