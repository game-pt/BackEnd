package com.a405.gamept.game.dto.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record PlayerStatusGetCommandDto(
        @NotBlank(message = "플레이어가 존재하지 않습니다.")
        String playerCode
) {
        public static PlayerStatusGetCommandDto of(String playerCode) {
            return PlayerStatusGetCommandDto.builder()
                    .playerCode(playerCode)
                    .build();
        }
}
