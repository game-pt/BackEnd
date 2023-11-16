package com.a405.gamept.game.dto.command;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * 사용자 또는 몬스터가 죽었는지 파악하기 위해 내부 로직에서 사용하는 record
 * @author : 지환
 */

@Builder
public record PlayerStateCommandDto(
        String nickname,
        int playerHp,
        int playerLevel,
        int playerExp,
        int statPoint,
        String prompt

) {
        public PlayerStateCommandDto of(String nickname, int playerHp, int playerLevel, int playerExp, int statPoint, String prompt){
                return PlayerStateCommandDto.builder()
                        .nickname(nickname)
                        .playerHp(playerHp)
                        .playerLevel(playerLevel)
                        .playerExp(playerExp)
                        .statPoint(statPoint)
                        .prompt(prompt)
                        .build();
        }
}
