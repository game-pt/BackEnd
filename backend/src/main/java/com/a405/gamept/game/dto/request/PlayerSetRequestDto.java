package com.a405.gamept.game.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record PlayerSetRequestDto(
        @NotBlank(message = "게임이 올바르지 않습니다.") String gameCode,
        @NotBlank(message = "종족이 올바르지 않습니다.") String raceCode,
        @NotBlank(message = "직업이 올바르지 않습니다.") String jobCode,
        @NotBlank(message = "닉네임이 올바르지 않습니다.") String nickname
) { }
