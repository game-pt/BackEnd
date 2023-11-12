package com.a405.gamept.game.dto.response;

import com.a405.gamept.play.entity.Player;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record PlayerStatusUpdateResponseDto(
        @NotBlank(message = "플레이어의 코드가 존재하지 않습니다.")
        String playerCode,
        @NotBlank(message = "플레이어의 레벨이 존재하지 않습니다.")
        int level,
        @NotBlank(message = "플레이어의 HP가 존재하지 않습니다.")
        int hp,
        @NotBlank(message = "플레이어의 경험치가 존재하지 않습니다.")
        int exp
) {
        public static PlayerStatusUpdateResponseDto of(Player player) {
                return PlayerStatusUpdateResponseDto.builder()
                        .playerCode(player.getCode())
                        .level(player.getLevel())
                        .hp(player.getHp())
                        .exp(player.getExp())
                        .build();
        }
}
