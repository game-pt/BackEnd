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
        @NotBlank(message = "스탯의 이름이 올바르지 않습니다.") @Size(min=1, max=4, message = "스탯의 이름은 1글자 이상, 4글자 이하여야 합니다.") String statName,
        @PositiveOrZero(message = "스탯의 값은 0 또는 양수여야 합니다.") @Max(value = 50, message = "스탯은 50을 넘을 수 없습니다.") int statValue
) {
    public static RaceStatGetResponseDto from(RaceStat raceStat) {
        return RaceStatGetResponseDto.builder()
                .statName(raceStat.getStat().getName())
                .statValue(raceStat.getStatValue())
                .build();
    }
}
