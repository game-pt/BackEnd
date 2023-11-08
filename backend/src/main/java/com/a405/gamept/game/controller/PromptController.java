package com.a405.gamept.game.controller;

import com.a405.gamept.game.dto.command.ChatCommandDto;
import com.a405.gamept.game.dto.command.PromptResultGetCommandDto;
import com.a405.gamept.game.dto.request.ChatRequestDto;
import com.a405.gamept.game.dto.request.PromptResultGetRequestDto;
import com.a405.gamept.game.dto.response.ChatResponseDto;
import com.a405.gamept.game.dto.response.PromptResultGetResponseDto;
import com.a405.gamept.game.service.PromptService;
import com.a405.gamept.game.util.exception.GameException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PromptController {

    private final PromptService promptService;

    private final SimpMessagingTemplate webSocket;

//    @PostMapping
//    public ResponseEntity<PromptResultGetResponseDto> getPromptResult(@Valid @RequestBody PromptResultGetRequestDto promptResultGetRequestDto) {
//        return ResponseEntity.ok(promptService.getPrmoptResult(PromptResultGetCommandDto.from(promptResultGetRequestDto)));
//    }

    @MessageMapping("/prompt/{gameCode}")
    @Deprecated
    public void getPromptResult(@Valid @Payload PromptResultGetRequestDto promptResultGetRequestDto, @Valid @DestinationVariable String gameCode) {
        PromptResultGetCommandDto promptResultGetCommandDto = PromptResultGetCommandDto.from(promptResultGetRequestDto, gameCode);
        PromptResultGetResponseDto promptResultGetResponseDto = promptService.getPrmoptResult(promptResultGetCommandDto);
        webSocket.convertAndSend("/topic/prompt/" + gameCode, promptResultGetResponseDto);
    }

}
