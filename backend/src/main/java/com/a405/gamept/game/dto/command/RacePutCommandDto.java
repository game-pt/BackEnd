package com.a405.gamept.game.dto.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record RacePutCommandDto (
        @NotBlank String roomCode,
        @NotBlank String playerCode,
        @NotBlank String raceCode
) {

}
