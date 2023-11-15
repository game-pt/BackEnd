package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.PromptListGetCommandDto;
import com.a405.gamept.game.dto.command.PromptResultGetCommandDto;
import com.a405.gamept.game.dto.response.PromptGetResponseDto;
import com.a405.gamept.game.dto.response.PromptResultGetResponseDto;
import com.a405.gamept.game.util.exception.GameException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface PromptService {
    PromptGetResponseDto setUserPrompt(PromptResultGetCommandDto promptResultGetCommandDto) throws GameException;
    PromptGetResponseDto getChatGPTPrompt(PromptResultGetCommandDto promptResultGetCommandDto) throws GameException;
    PromptResultGetResponseDto getPrmoptResult(PromptResultGetCommandDto promptResultGetCommandDto, String responsePrompt) throws GameException;
    List<PromptGetResponseDto> getPromptList(PromptListGetCommandDto promptListGetCommandDto) throws GameException;
    SseEmitter subscribeEmitter(String gameCode);
    void sendPrompt(String gameCode, String prompt) throws JsonProcessingException;
}
