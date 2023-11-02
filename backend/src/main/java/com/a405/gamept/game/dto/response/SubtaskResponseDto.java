package com.a405.gamept.game.dto.response;

import jakarta.validation.constraints.NotNull;

public record SubtaskResponseDto(
        @NotNull(message = "코드는 필수 입니다.")
        String code,
        @NotNull(message = "코드는 필수 입니다.")
        String name
) {
}
