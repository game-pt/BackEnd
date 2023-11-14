package com.a405.gamept.game.dto.response;

import com.a405.gamept.play.entity.Prompt;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;

@Builder
public record PromptGetResponseDto(
        @NotBlank(message = "역할이 존재하지 않습니다.")
        String role,
        @NotBlank(message = "내용이 존재하지 않습니다.")
        String content
) {
        public static PromptGetResponseDto from(Prompt prompt) {
                return PromptGetResponseDto.builder()
                        .role(prompt.getRole())
                        .content(prompt.getContent())
                        .build();
        }
}
