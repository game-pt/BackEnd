package com.a405.gamept.game.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;


@Builder
public record ChatResponseDto(
        @NotBlank(message = "게임코드가 올바르지 않습니다.")
        String gameCode,
        @NotBlank(message = "메시지 내용이 올바르지 않습니다.")
        String message
) { }
