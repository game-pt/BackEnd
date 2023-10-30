package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.entity.JobBonus;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record JobBonusGetResponseDto(
        @NotBlank(message = "보너스 스탯의 이름이 올바르지 않습니다.") @Size(min=1, max=4, message = "보너스 스탯의 이름은 1글자 이상, 4글자 이하여야 합니다.") String statName,
        @Min(value = -5, message = "보너스 스탯은 -5보다 낮을 수 없습니다.") @Max(value = 5, message = "보너스 스탯은 5보다 높을 수 없습니다.") int statBonus
) {
    public static JobBonusGetResponseDto from(JobBonus jobBonus) {
        return JobBonusGetResponseDto.builder()
                .statName(jobBonus.getStat().getName())
                .statBonus(jobBonus.getStatBonus())
                .build();
    }
}
