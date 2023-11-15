package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.util.RegexPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record PlayerStatUpdateCommandDto(
        @NotBlank(message = "게임이 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.GAME, message = "게임이 올바르지 않습니다.")
        String gameCode,
        @NotBlank(message = "플레이어가 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.PLAYER, message = "플레이어가 올바르지 않습니다.")
        String playerCode,
        @NotBlank(message = "스탯이 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.PLAYER, message = "스탯이 올바르지 않습니다.")
        String statCode,
        @NotBlank(message = "스탯량이 존재하지 않습니다.")
        int statValue
) {
        public static PlayerStatUpdateCommandDto of(String gameCode, String playerCode, String statCode, int statValue) {
                return PlayerStatUpdateCommandDto.builder()
                        .gameCode(gameCode)
                        .playerCode(playerCode)
                        .statCode(statCode)
                        .statValue(statValue)
                        .build();
        }
}
