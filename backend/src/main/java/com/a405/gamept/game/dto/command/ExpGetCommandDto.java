package com.a405.gamept.game.dto.command;

import com.a405.gamept.play.entity.Player;

public record ExpGetCommandDto(
        int playerExp,
        int playerLevel,
        int statPoint,
        String prompt
) {
    public ExpGetCommandDto of(int playerExp, int playerLevel, int statPoint, String prompt){
        return new ExpGetCommandDto(playerExp, playerLevel, statPoint, prompt);
    }
}
