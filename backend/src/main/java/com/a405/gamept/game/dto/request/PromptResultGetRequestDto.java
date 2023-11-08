package com.a405.gamept.game.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record PromptResultGetRequestDto(
        @NotBlank(message = "프롬프트가 입력되지 않았습니다.") String prompt
) { }
