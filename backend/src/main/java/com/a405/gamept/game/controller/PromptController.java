package com.a405.gamept.game.controller;

import com.a405.gamept.game.dto.command.GetPromptResultCommandDto;
import com.a405.gamept.game.dto.command.RaceGetCommandDto;
import com.a405.gamept.game.dto.request.GetPromptResultRequestDto;
import com.a405.gamept.game.service.PromptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("prompt")
@RequiredArgsConstructor
public class PromptController {

    private final PromptService promptService;

    public ResponseEntity<?> getPromptResult(@Valid GetPromptResultRequestDto getPromptResultRequestDto) {
        return ResponseEntity.ok(promptService.getPrmoptResult(GetPromptResultCommandDto.from(getPromptResultRequestDto)));
    }

}
