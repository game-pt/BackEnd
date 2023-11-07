package com.a405.gamept.game.dto.request;

import com.a405.gamept.game.dto.command.DiceGetCommandDto;
import jakarta.validation.constraints.NotNull;

public record DiceGetRequestDto(
        /**
         * playerCode : 현재 api를 요청한 사용자의 코드
         */
        @NotNull(message = "사용자의 코드를 입력해주세요.")
        String playerCode
) {
    public DiceGetCommandDto toCommand(String gameCode) {
        return new DiceGetCommandDto(gameCode, this.playerCode);
    }
}
