package com.a405.gamept.game.controller;

import com.a405.gamept.game.dto.request.ActGetRequestDto;
import com.a405.gamept.game.dto.request.DiceGetRequestDto;
import com.a405.gamept.game.service.GameService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/{gameCode}/{eventCode}")
    public ResponseEntity<?> getRaceList(@RequestBody @Valid ActGetRequestDto actGetRequestDto){
        return  ResponseEntity.ok(gameService.OptionsGet(actGetRequestDto.toCommand("001")));
    }

    @GetMapping("/dices")
    public ResponseEntity<?> getDicse(@RequestBody @Valid DiceGetRequestDto diceGetRequestDto){
        return ResponseEntity.ok(gameService.rollOfDice(diceGetRequestDto.toDto("001")));
    }
}
