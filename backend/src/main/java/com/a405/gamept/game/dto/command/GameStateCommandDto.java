package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.dto.request.GameSetRequestDto;
import com.a405.gamept.game.util.RegexPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record GameStateCommandDto(
        @NotBlank(message = "전투 상황은 필수입니다.")
        String prompt,
        @NotBlank(message = "전투 종료 여부는 필수입니다.")
        String endYn,
        PlayerStateCommandDto playerState,
        FightingEnemyStateCommandDto fightingEnemyState
) {
    public static GameStateCommandDto of(String prompt, String endYn, PlayerStateCommandDto playerStateCommandDto, FightingEnemyStateCommandDto fightingEnemyStateCommandDto) {
        return GameStateCommandDto.builder()
                .prompt(prompt)
                .endYn(endYn)
                .playerState(playerStateCommandDto)
                .fightingEnemyState(fightingEnemyStateCommandDto)
                .build();
    }
}
