package com.a405.gamept.game.dto.command;

import com.a405.gamept.play.entity.Player;

public record TmpPlayerCommandDto(
        Player player,
        String prompt
) {
    public TmpPlayerCommandDto of(Player player, String prompt){
        return new TmpPlayerCommandDto(player, prompt);
    }
}
