package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.entity.Story;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record StoryGetResponseDto(
        @NotBlank(message = "스토리 코드가 올바르지 않습니다.") String code,
        @NotBlank(message = "스토리 이름이 올바르지 않습니다.") @Size(min = 2, max = 30, message = "스토리의 이름은 2글자 이상, 30글자 이하여야 합니다.")String name,
        @NotBlank(message = "스토리 설명이 올바르지 않습니다.") String desc
) {
    public static StoryGetResponseDto from(Story story) {
        return StoryGetResponseDto.builder()
                .code(story.getCode())
                .name(story.getName())
                .desc(story.getDesc())
                .build();
    }
}
