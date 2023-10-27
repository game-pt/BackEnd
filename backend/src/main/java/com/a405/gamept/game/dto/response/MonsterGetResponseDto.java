package com.a405.gamept.game.dto.response;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record MonsterGetResponseDto(
        @NotBlank String name,
        @Positive @Max(10) int level
) { }
