package com.a405.gamept.game.controller;


import com.a405.gamept.game.dto.command.*;
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

@RestController
@RequestMapping("game")
public class GameController {
    private final SimpMessagingTemplate webSocket;
    private final GameService gameService;

    public GameController(SimpMessagingTemplate webSocket, GameService gameService) {
        this.webSocket = webSocket;
        this.gameService = gameService;
    }

    @GetMapping("/{gameCode}")
    public ResponseEntity<?> getActList(@PathVariable String gameCode, @Valid ActGetRequestDto actGetRequestDto){
        return  ResponseEntity.ok(gameService.getOptions(actGetRequestDto.toCommand(gameCode)));
    }

    @GetMapping("/{gameCode}/dices/{playerCode}")
    public ResponseEntity<?> getDices(@PathVariable String gameCode, @PathVariable String playerCode) {
        return ResponseEntity.ok(gameService.rollOfDice(DiceGetCommandDto.of(gameCode, playerCode)));
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
    @MessageMapping("/chat/{gameCode}")
    public void chat(@Payload ChatRequestDto chatRequestDto, @DestinationVariable String gameCode) throws GameException {
        ChatResponseDto chatResponseDto = gameService.chat(ChatCommandDto.from(gameCode, chatRequestDto));
        webSocket.convertAndSend("/topic/chat/" + gameCode, chatResponseDto.message());
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
