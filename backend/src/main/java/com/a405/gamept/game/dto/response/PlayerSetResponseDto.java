package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.util.RegexPatterns;
import com.a405.gamept.play.entity.Player;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record PlayerSetResponseDto(
        @NotBlank(message = "플레이어 코드가 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.PLAYER, message = "플레이어 코드가 올바르지 않습니다.")
        String playerCode
) {
        public static PlayerSetResponseDto from(Player player) {
                return PlayerSetResponseDto.builder()
                        .playerCode(player.getCode())
                        .build();
        }
}
