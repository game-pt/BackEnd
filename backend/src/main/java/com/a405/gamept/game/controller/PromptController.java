package com.a405.gamept.game.controller;

import com.a405.gamept.game.dto.command.ChatCommandDto;
import com.a405.gamept.game.dto.command.PromptListGetCommandDto;
import com.a405.gamept.game.dto.command.PromptResultGetCommandDto;
import com.a405.gamept.game.dto.request.ChatRequestDto;
import com.a405.gamept.game.dto.request.PromptListGetRequestDto;
import com.a405.gamept.game.dto.request.PromptResultGetRequestDto;
import com.a405.gamept.game.dto.response.ChatResponseDto;
import com.a405.gamept.game.dto.response.PromptGetResponseDto;
import com.a405.gamept.game.dto.response.PromptResultGetResponseDto;
import com.a405.gamept.game.service.PromptService;
import com.a405.gamept.game.util.exception.GameException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/prompt")
@RequiredArgsConstructor
public class PromptController {

    private final PromptService promptService;

    private final SimpMessagingTemplate webSocket;

//    @PostMapping
//    public ResponseEntity<PromptResultGetResponseDto> getPromptResult(@Valid @RequestBody PromptResultGetRequestDto promptResultGetRequestDto) {
//        return ResponseEntity.ok(promptService.getPrmoptResult(PromptResultGetCommandDto.from(promptResultGetRequestDto)));
//    }

    @MessageMapping("/prompt/{gameCode}")
    public void getPromptResult(@Valid @Payload PromptResultGetRequestDto promptResultGetRequestDto, @Valid @DestinationVariable String gameCode) {
        PromptResultGetCommandDto promptResultGetCommandDto = PromptResultGetCommandDto.from(promptResultGetRequestDto, gameCode);
        // 유저 프롬프트 보내기
        webSocket.convertAndSend("/topic/prompt/" + gameCode, promptService.setUserPrompt(promptResultGetCommandDto));
        PromptGetResponseDto promptGetResponseDto = promptService.getChatGPTPrompt(promptResultGetCommandDto);
        // ChatGPT 프롬프트 보내기
        webSocket.convertAndSend("/topic/prompt/" + gameCode, promptGetResponseDto);
        // 이벤트 발생 체크 후 보내기
        webSocket.convertAndSend("/topic/prompt/" + gameCode, promptService.getPrmoptResult(promptResultGetCommandDto, promptGetResponseDto.content()));
    }
    @GetMapping
    public ResponseEntity<?> getPromptList(@Valid PromptListGetRequestDto promptListGetRequestDto) throws GameException {
        return ResponseEntity.ok(promptService.getPromptList(PromptListGetCommandDto.from(promptListGetRequestDto)));
    }

    @GetMapping(value = "/subscribe/{gameCode}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable String gameCode) throws JsonProcessingException {
        return promptService.subscribeEmitter(gameCode);
    }

    @PostMapping("/send/{gameCode}")
    public void sendPrompt(@Valid @PathVariable String gameCode) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        promptService.sendPrompt(gameCode, mapper.writeValueAsString(PromptGetResponseDto.builder()
                .role("user")
                .content("게임 시작!")
                .build()));
    }

}
