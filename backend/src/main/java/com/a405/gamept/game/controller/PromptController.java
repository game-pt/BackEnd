package com.a405.gamept.game.controller;

import com.a405.gamept.game.dto.command.PromptResultGetCommandDto;
import com.a405.gamept.game.dto.request.PromptResultGetRequestDto;
import com.a405.gamept.game.dto.response.PromptResultGetResponseDto;
import com.a405.gamept.game.service.PromptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("prompt")
@RequiredArgsConstructor
public class PromptController {

    private final PromptService promptService;

    @PostMapping
    public ResponseEntity<PromptResultGetResponseDto> getPromptResult(@Valid @RequestBody PromptResultGetRequestDto promptResultGetRequestDto) {
        return ResponseEntity.ok(promptService.getPrmoptResult(PromptResultGetCommandDto.from(promptResultGetRequestDto)));
    }

}
