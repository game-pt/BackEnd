package com.a405.gamept.game.dto.command;

import jakarta.validation.constraints.NotBlank;

/**
 * 사용자 또는 몬스터가 죽었는지 파악하기 위해 내부 로직에서 사용하는 record
 * @author : 지환
 */

public record PlayerStateCommandDto(
        int playerHp,
        int playerExp
) {
        public PlayerStateCommandDto of(int playerHp, int playerExp){
                return new PlayerStateCommandDto(playerHp, playerExp);
        }

        public PlayerStateCommandDto from(PlayerStateCommandDto playerStateCommandDto){
                return new PlayerStateCommandDto(playerStateCommandDto.playerHp, playerStateCommandDto.playerExp);
        }
}
