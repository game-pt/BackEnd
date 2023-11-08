package com.a405.gamept.game.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ItemSetRequestDto (
        @NotBlank(message = "게임이 존재하지 않습니다.") String gameCode,
        @NotBlank(message = "플레이어가 존재하지 않습니다.") String playerCode
){ }
