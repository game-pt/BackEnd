package com.a405.gamept.game.dto.response;

import com.a405.gamept.play.entity.Player;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record PlayerSetResponseDto(
        @NotBlank(message = "플레이어 닉네임이 올바르지 않습니다.")
        @Size(min = 2, max = 10, message = "플레이어의 닉네임은 2글자 이상, 10글자 이하여야 합니다.")
        String nickname
) {
        public static PlayerSetResponseDto from(Player player) {
                return PlayerSetResponseDto.builder()
                        .nickname(player.getNickname())
                        .build();
        }
}
