package com.a405.gamept.game.dto.response;

public record DiceGetResponseDto(
        int dice1,
        int dice2
) {
    public static DiceGetResponseDto of(int dice1, int dice2){
        return new DiceGetResponseDto(
                dice1,
                dice2
        );
    }
}
