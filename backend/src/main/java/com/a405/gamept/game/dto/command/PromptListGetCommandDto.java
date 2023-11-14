package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.dto.request.PromptListGetRequestDto;
import com.a405.gamept.game.util.RegexPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record PromptListGetCommandDto(
        @NotBlank(message = "게임이 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.GAME, message = "게임이 올바르지 않습니다.")
        String gameCode,
        @NotBlank(message = "플레이어가 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.PLAYER, message = "플레이어가 올바르지 않습니다.")
        String playerCode
) {
        public static PromptListGetCommandDto from(PromptListGetRequestDto promptListGetRequestDto) {
                return PromptListGetCommandDto.builder()
                        .gameCode(promptListGetRequestDto.gameCode())
                        .playerCode(promptListGetRequestDto.playerCode())
                        .build();
        }
}
