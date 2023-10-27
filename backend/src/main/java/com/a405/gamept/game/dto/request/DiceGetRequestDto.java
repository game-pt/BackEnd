package com.a405.gamept.game.dto.request;

import com.a405.gamept.game.dto.command.DiceGetCommandDto;

public record DiceGetRequestDto(
        /**
         * gameCode : 현재 진행 중인 게임코드
         */
        String gameCode
) {
    public DiceGetCommandDto toDto() {
        return new DiceGetCommandDto(this.gameCode);
    }
}
