package com.a405.gamept.game.controller;

import com.a405.gamept.game.dto.command.JobGetCommandDto;
import com.a405.gamept.game.dto.command.PlayerGetCommandDto;
import com.a405.gamept.game.dto.command.PlayerSetCommandDto;
import com.a405.gamept.game.dto.command.RaceGetCommandDto;
import com.a405.gamept.game.dto.request.JobGetRequestDto;
import com.a405.gamept.game.dto.request.PlayerGetRequestDto;
import com.a405.gamept.game.dto.request.PlayerSetRequestDto;
import com.a405.gamept.game.dto.request.RaceGetRequestDto;
import com.a405.gamept.game.service.PlayerService;
import com.a405.gamept.game.util.exception.GameException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("player")
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
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

    /*
    @GetMapping("skill")
    public ResponseEntity<?> getSkill(@Valid ) throws GameException {
        return ResponseEntity.ok(playerService.getJobList(JobGetCommandDto.from(jobGetRequestDto)));
    }
    */

    @PostMapping
    public ResponseEntity<?> setPlayer(@Valid @RequestBody PlayerSetRequestDto playerSetRequestDto) throws GameException {
        return ResponseEntity.ok(playerService.setPlayer(PlayerSetCommandDto.from(playerSetRequestDto)));
    }

    @GetMapping
    public ResponseEntity<?> getPlayer(@Valid PlayerGetRequestDto playerGetRequestDto) throws GameException {
        return ResponseEntity.ok(playerService.getPlayer(PlayerGetCommandDto.from(playerGetRequestDto)));
    }
}
