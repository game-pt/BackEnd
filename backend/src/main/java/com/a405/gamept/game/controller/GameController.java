package com.a405.gamept.game.controller;

import com.a405.gamept.game.dto.command.ChatCommandDto;
import com.a405.gamept.game.dto.command.GameSetCommandDto;
import com.a405.gamept.game.dto.command.StoryGetCommandDto;
import com.a405.gamept.game.dto.request.*;
import com.a405.gamept.game.dto.response.ChatResponseDto;
import com.a405.gamept.game.service.GameService;
import com.a405.gamept.game.util.exception.GameException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("game")
public class GameController {
    private final SimpMessagingTemplate webSocket;
    private final GameService gameService;

    public GameController(SimpMessagingTemplate webSocket, GameService gameService) {
        this.webSocket = webSocket;
        this.gameService = gameService;
    }

    @GetMapping("/{gameCode}/{eventCode}")
    public ResponseEntity<?> getActList(@Valid ActGetRequestDto actGetRequestDto){
        return  ResponseEntity.ok(gameService.getOptions(actGetRequestDto.toCommand("001")));
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
    @MessageMapping("/chat/{gameCode}")
    public void chat(@Payload ChatRequestDto chatRequestDto, @DestinationVariable String gameCode) throws GameException {
        ChatResponseDto chatResponseDto = gameService.chat(ChatCommandDto.from(gameCode, chatRequestDto));
        webSocket.convertAndSend("/topic/chat/" + gameCode, chatResponseDto.message());
    }
    @GetMapping("/subtask")
    public ResponseEntity<?> getSubtask(SubtaskRequestDto subtaskRequestDto) {
        return ResponseEntity.ok(gameService.getSubtask(subtaskRequestDto.toCommand("001")));
    }

    @PostMapping
    public ResponseEntity<?> setGame(@RequestBody @Valid GameSetRequestDto gameSetRequestDto) {
        return ResponseEntity.ok(gameService.setGame(GameSetCommandDto.from(gameSetRequestDto)));
    }
}
