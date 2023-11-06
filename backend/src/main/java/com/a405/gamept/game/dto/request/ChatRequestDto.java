package com.a405.gamept.game.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChatRequestDto(
        @NotBlank(message = "플레이어 코드가 올바르지 않습니다.")
        String playerCode,
        @NotBlank(message = "메시지 내용가 올바르지 않습니다.")
        String message
) { }
