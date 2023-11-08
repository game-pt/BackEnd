package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.entity.Monster;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record MonsterGetResponseDto(
        @JsonProperty("name") @NotBlank(message = "몬스터 이름이 올바르지 않습니다.") String monsterName,
        @JsonProperty("level") @Positive(message = "몬스터 레벨은 양수여야 합니다.") @Max(value = 10, message = "몬스터 레벨은 10을 넘을 수 없습니다.") int monsterLevel
) {
    public static MonsterGetResponseDto from(Monster monster) {
        return MonsterGetResponseDto.builder()
                .monsterLevel(monster.getLevel())
                .build();
    }
}
