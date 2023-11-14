package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.entity.Stat;
import com.a405.gamept.game.util.GameData;
import com.a405.gamept.game.util.RegexPatterns;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record StatGetResponseDto(
        @NotBlank(message = "스탯 코드가 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.STAT, message = "스탯 코드가 올바르지 않습니다.")
        String statCode,
        @NotBlank(message = "스탯명이 존재하지 않습니다.")
        @Size(min=1, max=3, message = "스탯명은 1글자 이상 3글자 이하여야 합니다.")
        String statName,
        @PositiveOrZero(message = "스탯 값은 양수여야 합니다.")
        @Max(value = GameData.MAX_STAT, message = "스탯 값은 " + GameData.MAX_STAT + "을 넘을 수 없습니다.")
        int statValue
) {
        public static StatGetResponseDto from(Stat stat, int value) {
                return StatGetResponseDto.builder()
                        .statCode(stat.getCode())
                        .statName(stat.getName())
                        .statValue(value)
                        .build();
        }
}
