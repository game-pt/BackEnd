package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.entity.Race;
import com.a405.gamept.game.entity.RaceStat;
import com.a405.gamept.game.util.RegexPatterns;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record PlayerRaceGetResponseDto(
        @NotBlank(message = "종족 코드가 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.RACE, message = "종족 코드가 올바르지 않습니다.")
        String code,
        @NotBlank(message = "종족명이 존재하지 않습니다.")
        @Size(min = 2, max = 10, message = "종족의 이름은 2글자 이상, 10글자 이하여야 합니다.")
        String name
) {
    public static PlayerRaceGetResponseDto from(Race race) {
        return PlayerRaceGetResponseDto.builder()
                .code(race.getCode())
                .name(race.getName())
                .build();
    }
}
