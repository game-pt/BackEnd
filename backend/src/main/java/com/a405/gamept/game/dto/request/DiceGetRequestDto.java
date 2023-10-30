package com.a405.gamept.game.dto.request;

import com.a405.gamept.game.dto.command.DiceGetCommandDto;
import jakarta.validation.constraints.NotNull;

public record DiceGetRequestDto(
        /**
         * gameCode : 현재 진행 중인 게임코드
         */
        @NotNull(message = "참여중인 게임의 코드를 입력해주세요.")
        String gameCode
) {
    public DiceGetCommandDto toDto() {
        return new DiceGetCommandDto(this.gameCode);
    }
}
