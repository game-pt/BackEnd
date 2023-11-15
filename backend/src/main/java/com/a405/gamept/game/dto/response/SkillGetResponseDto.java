package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.entity.Skill;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record SkillGetResponseDto (
        @NotBlank(message = "스킬명이 존재하지 않습니다.")
        @Size(max = 20, message = "스킬명은 최대 20자여야 합니다.")
        String name,

        @NotBlank(message = "스킬 설명이 존재하지 않습니다.")
        String desc
) {
        public static SkillGetResponseDto from (Skill skill) {
                return SkillGetResponseDto.builder()
                        .name(skill.getName())
                        .desc(skill.getDesc())
                        .build();
        }
}
