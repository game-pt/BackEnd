package com.a405.gamept.game.dto.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record PlayerLevelUpdateCommand(
        @NotBlank(message = "플레이어의 코드가 존재하지 않습니다.")
        String playerCode
) {
    public static PlayerLevelUpdateCommand of(String playerCode) {
        return PlayerLevelUpdateCommand.builder()
                .playerCode(playerCode)
                .build();
    }
}
