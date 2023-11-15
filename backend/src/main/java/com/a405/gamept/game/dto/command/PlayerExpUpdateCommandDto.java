package com.a405.gamept.game.dto.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record PlayerExpUpdateCommandDto(
        @NotBlank(message = "플레이어의 코드가 존재하지 않습니다.")
        String playerCode,
        @NotBlank(message = "플레이어의 경험치 증가량이 존재하지 않습니다.")
        int changeAmount
) {
    public static PlayerExpUpdateCommandDto of(String playerCode, int changeAmount) {
        return PlayerExpUpdateCommandDto.builder()
                .playerCode(playerCode)
                .changeAmount(changeAmount)
                .build();
    }
}
