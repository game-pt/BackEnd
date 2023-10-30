package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.entity.Monster;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record MonsterGetResponseDto(
        @NotBlank(message = "몬스터 이름이 올바르지 않습니다.") String name,
        @Positive(message = "몬스터 레벨은 양수여야 합니다.") @Max(value = 10, message = "몬스터 레벨은 10을 넘을 수 없습니다.") int level
) {
    public static MonsterGetResponseDto from(Monster monster) {
        return MonsterGetResponseDto.builder()
                .name(monster.getName())
                .level(monster.getLevel())
                .build();
    }
}
