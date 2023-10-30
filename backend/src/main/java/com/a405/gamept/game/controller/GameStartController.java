package com.a405.gamept.game.controller;

import com.a405.gamept.game.dto.command.RaceGetCommandDto;
import com.a405.gamept.game.dto.request.RaceGetRequestDto;
import com.a405.gamept.game.service.GameStartService;
import com.a405.gamept.game.util.exception.GameException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("start")
public class GameStartController {

    private final GameStartService gameStartService;

    @Autowired
    public GameStartController(GameStartService gameStartService) {
        this.gameStartService = gameStartService;
    }

    @GetMapping("race")
    public ResponseEntity<?> getRaceList(@Valid RaceGetRequestDto raceGetRequestDto) throws GameException {
        return ResponseEntity.ok(gameStartService.getRaceList(RaceGetCommandDto.from(raceGetRequestDto)));
    }
}
