package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.entity.Monster;
import com.a405.gamept.game.util.GameData;
import com.a405.gamept.game.util.RegexPatterns;
import com.a405.gamept.play.entity.FightingEnermy;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record MonsterGetResponseDto(
        @NotBlank(message = "몬스터가 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.FIGHTING_ENEMY, message = "몬스터가 올바르지 않습니다.")
        String code,
        @JsonProperty("level")
        @Positive(message = "몬스터 레벨은 양수여야 합니다.")
        @Max(value = GameData.MONSTER_MAX_LEVEL, message = "몬스터 레벨은 " + GameData.MONSTER_MAX_LEVEL + "을 넘을 수 없습니다.")
        int monsterLevel,
        @JsonProperty("hp")
        @PositiveOrZero(message = "몬스터 체력은 양수여야 합니다.")
        int hp,
        @JsonProperty("attack")
        @Positive(message = "몬스터 공격력은 양수여야 합니다.")
        int monsterAttack

) {
    public static MonsterGetResponseDto from(FightingEnermy fightingEnermy) {
        return MonsterGetResponseDto.builder()
                .code(fightingEnermy.getCode())
                .monsterLevel(fightingEnermy.getLevel())
                .hp(fightingEnermy.getHp())
                .monsterAttack(fightingEnermy.getAttack())
                .build();
    }
}
