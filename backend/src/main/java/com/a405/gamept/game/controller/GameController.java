package com.a405.gamept.game.controller;

import com.a405.gamept.game.dto.command.RaceGetCommandDto;
import com.a405.gamept.game.dto.request.RaceGetRequestDto;
import com.a405.gamept.game.service.GameService;
import com.a405.gamept.global.error.exception.BadRequestException;
import com.a405.gamept.global.error.exception.InternalServerException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("player/race")
    public ResponseEntity<?> getRaceList(@Valid RaceGetRequestDto raceGetRequestDto) throws InternalServerException, BadRequestException {
        return ResponseEntity.ok(gameService.getRaceList(RaceGetCommandDto.from(raceGetRequestDto)));
    }
}
