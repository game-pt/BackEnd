package com.a405.gamept.game.dto.request;

import com.a405.gamept.game.util.RegexPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;

public record GameSetRequestDto(
        @NotBlank(message = "스토리가 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.STORY, message = "스토리가 올바르지 않습니다.")
        String storyCode
) { }
