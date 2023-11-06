package com.a405.gamept.game.dto.command;

public record DiceGetCommandDto(
        /**
         * gameCode : 현재 진행 중인 게임코드
         */
        String gameCode,
        String playerCode
) {
    public static DiceGetCommandDto from(String gameCode, String playerCode){
        return new DiceGetCommandDto(gameCode, playerCode);
    }
}
