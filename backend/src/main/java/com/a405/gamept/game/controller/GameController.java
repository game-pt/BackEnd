package com.a405.gamept.game.controller;

import com.a405.gamept.game.dto.command.GameSetCommandDto;
import com.a405.gamept.game.dto.command.StoryGetCommandDto;
import com.a405.gamept.game.dto.request.GameSetRequestDto;
import com.a405.gamept.game.service.GameService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.a405.gamept.game.dto.request.ActGetRequestDto;
import com.a405.gamept.game.dto.request.DiceGetRequestDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("game")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/{gameCode}/{eventCode}")
    public ResponseEntity<?> getActList(@Valid ActGetRequestDto actGetRequestDto){
        return  ResponseEntity.ok(gameService.OptionsGet(actGetRequestDto.toCommand("001")));
    }

    @GetMapping("/dices")
    public ResponseEntity<?> getDices(@Valid DiceGetRequestDto diceGetRequestDto) {
        return ResponseEntity.ok(gameService.rollOfDice(diceGetRequestDto.toDto("001")));
    }
    @GetMapping("story")
    public ResponseEntity<?> getStoryList() {
        return ResponseEntity.ok(gameService.getStoryList());
    }
    @GetMapping("story/{storyCode}")
    public ResponseEntity<?> getStory(@PathVariable String storyCode) {
        return ResponseEntity.ok(gameService.getStory(StoryGetCommandDto.of(storyCode)));
    }
    @PostMapping
    public ResponseEntity<?> setGame(@RequestBody @Valid GameSetRequestDto gameSetRequestDto) {
        return ResponseEntity.ok(gameService.setGame(GameSetCommandDto.from(gameSetRequestDto)));
    }
}
