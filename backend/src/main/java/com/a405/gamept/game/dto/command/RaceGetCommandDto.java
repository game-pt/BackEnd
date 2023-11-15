package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.dto.request.RaceGetRequestDto;
import com.a405.gamept.game.util.RegexPatterns;
import com.a405.gamept.play.entity.Game;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record RaceGetCommandDto(
        @NotBlank(message = "게임이 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.GAME, message = "게임이 올바르지 않습니다.")
        String gameCode
) {
    public static RaceGetCommandDto from(RaceGetRequestDto raceGetRequestDto) {
        return RaceGetCommandDto.builder()
                .gameCode(raceGetRequestDto.gameCode())
                .build();
    }
}
