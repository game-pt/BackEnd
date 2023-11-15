package com.a405.gamept.game.dto.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record PlayerHpUpdateCommandDto(
        @NotBlank(message = "플레이어의 코드가 존재하지 않습니다.")
        String playerCode,
        @NotBlank(message = "플레이어의 HP 증감량이 존재하지 않습니다.")
        int changeAmount
) {
    public static PlayerHpUpdateCommandDto of(String playerCode, int changeAmount) {
        return PlayerHpUpdateCommandDto.builder()
                .playerCode(playerCode)
                .changeAmount(changeAmount)
                .build();
    }
}
