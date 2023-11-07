package com.a405.gamept.game.controller;

import com.a405.gamept.game.dto.command.DiceGetCommandDto;
import com.a405.gamept.game.dto.command.GameSetCommandDto;
import com.a405.gamept.game.dto.command.StoryGetCommandDto;
import com.a405.gamept.game.dto.request.ActGetRequestDto;
import com.a405.gamept.game.dto.request.ActResultGetRequestDto;
import com.a405.gamept.game.dto.request.DiceGetRequestDto;
import com.a405.gamept.game.dto.request.SubtaskRequestDto;
import com.a405.gamept.game.dto.request.GameSetRequestDto;
import com.a405.gamept.game.service.GameService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/{gameCode}")
    public ResponseEntity<?> getActList(@PathVariable String gameCode, @Valid ActGetRequestDto actGetRequestDto){
        return  ResponseEntity.ok(gameService.getOptions(actGetRequestDto.toCommand(gameCode)));
    }

    @GetMapping("/{gameCode}/dices/{playerCode}")
    public ResponseEntity<?> getDices(@PathVariable String gameCode, @PathVariable String playerCode) {
        return ResponseEntity.ok(gameService.rollOfDice(new DiceGetCommandDto(gameCode, playerCode)));
    }
    @GetMapping("story")
    public ResponseEntity<?> getStoryList() {
        return ResponseEntity.ok(gameService.getStoryList());
    }
    @GetMapping("story/{storyCode}")
    public ResponseEntity<?> getStory(@PathVariable String storyCode) {
        return ResponseEntity.ok(gameService.getStory(StoryGetCommandDto.of(storyCode)));
    }
    @GetMapping("/{gameCode}/subtask")
    public ResponseEntity<?> getSubtask(@PathVariable String gameCode, @Valid SubtaskRequestDto subtaskRequestDto) {
        return ResponseEntity.ok(gameService.getSubtask(subtaskRequestDto.toCommand(gameCode)));
    }

    @PostMapping
    public ResponseEntity<?> setGame(@RequestBody @Valid GameSetRequestDto gameSetRequestDto) {
        return ResponseEntity.ok(gameService.setGame(GameSetCommandDto.from(gameSetRequestDto)));
    }

    @GetMapping("/{gameCode}/play")
    public ResponseEntity<?> playGame(@PathVariable String gameCode, @Valid ActResultGetRequestDto actResultGetRequestDto) {
        return ResponseEntity.ok(gameService.playAct(actResultGetRequestDto.toCommand(gameCode)));
    }
}
