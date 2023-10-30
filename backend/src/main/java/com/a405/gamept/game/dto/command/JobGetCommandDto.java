package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.dto.request.JobGetRequestDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record JobGetCommandDto(
        @NotBlank(message = "스토리가 입력되지 않았습니다.") /*@Size(7)*/ String storyCode
) {
    public static JobGetCommandDto from(JobGetRequestDto jobGetRequestDto) {
        return JobGetCommandDto.builder()
                .storyCode(jobGetRequestDto.storyCode())
                .build();
    }
}
