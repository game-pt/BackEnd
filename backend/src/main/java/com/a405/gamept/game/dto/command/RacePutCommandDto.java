package com.a405.gamept.game.dto.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record RacePutCommandDto (
        @NotBlank(message = "방이 입력되지 않았습니다.") String roomCode,
        @NotBlank(message = "플레이어가 입력되지 않았습니다.") String playerCode,
        @NotBlank(message = "종족이 선택되지 않았습니다.") String raceCode
) {

}
