package com.a405.gamept.game.controller;

import com.a405.gamept.game.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("game")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("story")
    public ResponseEntity<?> getStoryList() {
        return ResponseEntity.ok(gameService.getStoryList());
    }
}
