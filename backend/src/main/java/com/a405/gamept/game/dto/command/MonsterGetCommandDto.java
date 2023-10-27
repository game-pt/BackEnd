package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.util.FinalData;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record MonsterGetCommandDto(
        @NotBlank String storyCode,
        @Positive @Max(FinalData.MONSTER_MAX_LEVEL) int level
) { }
