package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.entity.RaceStat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record RaceStatGetResponseDto(
        @NotBlank @Size(min=1, max=4) String statName,
        @PositiveOrZero @Max(50) int statValue
) {
    public static RaceStatGetResponseDto from(RaceStat raceStat) {
        return RaceStatGetResponseDto.builder()
                .statName(raceStat.getStat().getName())
                .statValue(raceStat.getStatValue())
                .build();
    }
}
