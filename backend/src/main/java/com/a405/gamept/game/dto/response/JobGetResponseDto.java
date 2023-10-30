package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.entity.Job;
import com.a405.gamept.game.entity.JobBonus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record JobGetResponseDto(
        @NotBlank(message = "직업 코드가 올바르지 않습니다.") /*@Size(7)*/ String code,
        @NotBlank(message = "직업 이름이 올바르지 않습니다.") @Size(min = 2, max = 10, message = "직업의 이름은 2글자 이상, 10글자 이하여야 합니다.") String name,
        @Valid @Size(min = 1, message = "추가 스탯이 최소 한 개 이상 존재해야 합니다.") List<JobBonusGetResponseDto> bonusList
) {
    public static JobGetResponseDto from(Job job) {
        List<JobBonus> bonusList = job.getJobBonusList();
        List<JobBonusGetResponseDto> bonusGetResponseDtoList = new ArrayList<>();

        for (JobBonus bonus : bonusList) {
            bonusGetResponseDtoList.add(JobBonusGetResponseDto.from(bonus));
        }

        return JobGetResponseDto.builder()
                .code(job.getCode())
                .name(job.getName())
                .bonusList(bonusGetResponseDtoList)
                .build();
    }
}
