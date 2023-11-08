package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.dto.request.PlayerSetRequestDto;
import com.a405.gamept.game.util.RegexPatterns;
import com.a405.gamept.play.entity.Game;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record PlayerSetCommandDto(
        @NotBlank(message = "게임이 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.GAME, message = "게임이 올바르지 않습니다.")
        String gameCode,
        @NotBlank(message = "종족이 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.RACE, message = "종족이 올바르지 않습니다.")
        String raceCode,
        @NotBlank(message = "직업이 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.JOB, message = "직업이 올바르지 않습니다.")
        String jobCode,
        @NotBlank(message = "닉네임이 존재하지 않습니다.")
        @Size(min = 1, max = 20, message = "닉네임은 1글자 이상, 20글자 이하여야 합니다.")
        String nickname
) {
    public static PlayerSetCommandDto from(PlayerSetRequestDto playerSetRequestDto) {
        return PlayerSetCommandDto.builder()
                .gameCode(playerSetRequestDto.gameCode())
                .raceCode(playerSetRequestDto.raceCode())
                .jobCode(playerSetRequestDto.jobCode())
                .nickname(playerSetRequestDto.nickname())
                .build();
    }
}
