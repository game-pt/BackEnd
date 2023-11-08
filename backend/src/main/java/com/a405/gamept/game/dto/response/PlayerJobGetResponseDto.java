package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.entity.Job;
import com.a405.gamept.game.entity.JobBonus;
import com.a405.gamept.game.entity.Skill;
import com.a405.gamept.game.util.RegexPatterns;
import com.a405.gamept.util.ValidateUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record PlayerJobGetResponseDto(
        @NotBlank(message = "직업 코드가 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.JOB, message = "직업 코드가 올바르지 않습니다.")
        String code,
        @NotBlank(message = "직업명이 올바르지 않습니다.")
        @Size(min = 2, max = 10, message = "직업명은 2글자 이상, 10글자 이하여야 합니다.")
        String name,
        @Valid
        @Size(min = 4, max = 4, message = "스킬은 4개가 존재해야 합니다.")
        List<SkillGetResponseDto> skillList
) {
    public static PlayerJobGetResponseDto from(Job job) {
        // 스킬 삽입
        List<Skill> skillList = job.getSkillList();
        List<SkillGetResponseDto> skillGetResponseDtoList = new ArrayList<>();

        SkillGetResponseDto skillGetResponseDto;
        for (Skill skill : skillList) {
            skillGetResponseDto = SkillGetResponseDto.from(skill);
            ValidateUtil.validate(skillGetResponseDto);

            skillGetResponseDtoList.add(skillGetResponseDto);
        }

        return PlayerJobGetResponseDto.builder()
                .code(job.getCode())
                .name(job.getName())
                .skillList(skillGetResponseDtoList)
                .build();
    }
}
