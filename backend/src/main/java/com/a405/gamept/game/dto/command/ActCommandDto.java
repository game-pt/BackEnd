package com.a405.gamept.game.dto.command;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
public record ActCommandDto(
        @NotBlank(message = "행동 코드가 입력되지 않았습니다.") String code,
        @NotBlank(message = "행동 이름이 입력되지 않았습니다.") String name
) {
}
