package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.dto.request.GetPromptResultRequestDto;
import com.a405.gamept.game.entity.Event;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record GetPromptResultCommandDto(

        @NotBlank(message = "게임 코드가 입력되지 않았습니다.") String code,
        @NotBlank(message = "프롬프트가 입력되지 않았습니다.") String prompt,

        EventCommandDto eventCommandDto
) {
    public static GetPromptResultCommandDto from(GetPromptResultRequestDto getPromptResultRequestDto) {
        return GetPromptResultCommandDto.builder()
                .code(getPromptResultRequestDto.code())
                .prompt(getPromptResultRequestDto.prompt())
                .eventCommandDto(null)
                .build();
    }

    public static GetPromptResultCommandDto from(GetPromptResultCommandDto getPromptResultCommandDto, String prompt) {
        return GetPromptResultCommandDto.builder()
                .code(getPromptResultCommandDto.code())
                .prompt(prompt)
                .eventCommandDto(getPromptResultCommandDto.eventCommandDto())
                .build();
    }

    public static GetPromptResultCommandDto from(GetPromptResultCommandDto getPromptResultCommandDto, EventCommandDto eventCommandDto) {
        return GetPromptResultCommandDto.builder()
                .code(getPromptResultCommandDto.code())
                .prompt(getPromptResultCommandDto.prompt())
                .eventCommandDto(eventCommandDto)
                .build();
    }

}
