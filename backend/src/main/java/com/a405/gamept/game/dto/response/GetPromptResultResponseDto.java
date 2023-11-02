package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.dto.command.EventCommandDto;
import com.a405.gamept.game.dto.command.GetPromptResultCommandDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record GetPromptResultResponseDto(
        @NotBlank(message = "게임 코드가 입력되지 않았습니다.") String code,
        @NotBlank(message = "프롬프트가 입력되지 않았습니다.") String prompt,
        EventCommandDto event
) {
    public static GetPromptResultResponseDto from(GetPromptResultCommandDto getPromptResultCommandDto, EventCommandDto eventCommandDto) {
        return GetPromptResultResponseDto.builder()
                .code(getPromptResultCommandDto.code())
                .prompt(getPromptResultCommandDto.prompt())
                .event(eventCommandDto)
                .build();
    }
}
