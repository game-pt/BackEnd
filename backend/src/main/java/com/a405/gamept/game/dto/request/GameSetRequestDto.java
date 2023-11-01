package com.a405.gamept.game.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;

public record GameSetRequestDto(
        @NotBlank(message = "스토리가 존재하지 않습니다.") String storyCode
) { }
