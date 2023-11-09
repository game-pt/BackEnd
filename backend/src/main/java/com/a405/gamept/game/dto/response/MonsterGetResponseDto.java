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
        @NotBlank(message = "G몬스터 코드는 필수 입니다.") String gmonsterCode,
        @JsonProperty("level") @Positive(message = "몬스터 레벨은 양수여야 합니다.") @Max(value = 10, message = "몬스터 레벨은 10을 넘을 수 없습니다.") int monsterLevel,
        @JsonProperty("attack") @Positive(message = "몬스터 공격력은 양수여야 합니다.") int monsterAttack

) {
    public static MonsterGetResponseDto from(Monster monster, String gmonsterCode) {
        return MonsterGetResponseDto.builder()
                .gmonsterCode(gmonsterCode)
                .monsterLevel(monster.getLevel())
                .monsterAttack(monster.getAttack())
                .build();
    }
}
