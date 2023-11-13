package com.a405.gamept.game.controller;


import com.a405.gamept.game.dto.command.*;
import com.a405.gamept.game.dto.request.*;
import com.a405.gamept.game.dto.response.*;
import com.a405.gamept.game.service.FightService;
import com.a405.gamept.game.service.GameService;
import com.a405.gamept.game.service.PlayerService;
import com.a405.gamept.game.util.exception.GameException;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("game")
public class GameController {
    private final SimpMessagingTemplate webSocket;
    private final GameService gameService;
    private final FightService fightService;
    private final PlayerService playerService;

    @GetMapping("/{gameCode}")
    public ResponseEntity<?> getActList(@PathVariable String gameCode, @Valid ActGetRequestDto actGetRequestDto){
        return  ResponseEntity.ok(gameService.getOptions(actGetRequestDto.toCommand(gameCode)));
    }

    @MessageMapping("/dice/{gameCode}")
    public void getDices(@DestinationVariable String gameCode, @Valid @Payload DiceGetRequestDto diceGetRequestDto) {
        DiceGetResponseDto diceGetResponseDto = gameService.rollOfDice(diceGetRequestDto.toCommand(gameCode));
        webSocket.convertAndSend("/topic/dice/" + gameCode, diceGetResponseDto);
    }
    @GetMapping("story")
    public ResponseEntity<?> getStoryList() {
        return ResponseEntity.ok(gameService.getStoryList());
    }
    @GetMapping("story/{storyCode}")
    public ResponseEntity<?> getStory(@PathVariable String storyCode) {
        return ResponseEntity.ok(gameService.getStory(StoryGetCommandDto.of(storyCode)));
    }

    @MessageMapping("/subtask/{gameCode}")
    public void getSubtask(@DestinationVariable String gameCode, @Valid @Payload SubtaskRequestDto subtaskRequestDto) {
        List<SubtaskResponseDto> subtaskResponseDto = gameService.getSubtask(subtaskRequestDto.toCommand(gameCode));
        webSocket.convertAndSend("/topic/subtask/"+gameCode, subtaskResponseDto);
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

    @MessageMapping("/select/{gameCode}")
    public void playGame(@DestinationVariable String gameCode, @Valid @Payload ActResultGetRequestDto actResultGetRequestDto) {
        ActResultGetResponseDto actResultGetResponseDto = gameService.playAct(actResultGetRequestDto.toCommand(gameCode));
        webSocket.convertAndSend("/topic/select/"+gameCode, actResultGetResponseDto);
    }

    @MessageMapping("/fight/{gameCode}")
    public void playFight(@DestinationVariable String gameCode, @Valid @Payload FightResultGetRequestDto fightResultGetRequestDto) {
        FightResultGetResponseDto fightResultGetResponseDto = fightService.getFightResult(fightResultGetRequestDto.toCommand(gameCode));
        PlayerStatusGetResponseDto playerStatusGetResponseDto = playerService.getPlayerStatus(PlayerStatusGetCommandDto.of(fightResultGetRequestDto.playerCode()));
        webSocket.convertAndSend("/topic/fight/" + gameCode, fightResultGetResponseDto);
        webSocket.convertAndSendToUser(fightResultGetRequestDto.playerCode(), "/status", playerStatusGetResponseDto);
        //return ResponseEntity.ok(fightService.getFightResult(fightResultGetRequestDto.toCommand(gameCode)));
    }

    /*
    @PostMapping("/monster")
    public ResponseEntity<?> setMonster(@Valid @RequestBody MonsterSetRequestDto monsterSetRequestDto) {
        fightService.setMonster(MonsterSetCommandDto.from(monsterSetRequestDto));
        return ResponseEntity.ok(true);
    }
     */
}
