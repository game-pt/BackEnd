package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.entity.Event;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
public record StatChangeCommandDto(
        String prompt,
        int playerHp,
        Map<String, Integer> playerStat
) {
    public static StatChangeCommandDto of(String prompt, int playerHp, Map<String, Integer> playerStat) {
        return StatChangeCommandDto.builder()
                .prompt(prompt)
                .playerHp(playerHp)
                .playerStat(playerStat)
                .build();
    }
}