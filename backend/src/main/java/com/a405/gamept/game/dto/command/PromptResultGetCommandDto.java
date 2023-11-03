package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.dto.request.PromptResultGetRequestDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record PromptResultGetCommandDto(

        @NotBlank(message = "게임 코드가 입력되지 않았습니다.") String gameCode,
        @NotBlank(message = "프롬프트가 입력되지 않았습니다.") String prompt,
        EventCommandDto eventCommandDto
) {
    public static PromptResultGetCommandDto from(PromptResultGetRequestDto promptResultGetRequestDto) {
        return PromptResultGetCommandDto.builder()
                .gameCode(promptResultGetRequestDto.gameCode())
                .prompt(promptResultGetRequestDto.prompt())
                .eventCommandDto(null)
                .build();
    }

    public static PromptResultGetCommandDto from(PromptResultGetCommandDto promptResultGetCommandDto, String prompt) {
        return PromptResultGetCommandDto.builder()
                .gameCode(promptResultGetCommandDto.gameCode())
                .prompt(prompt)
                .eventCommandDto(promptResultGetCommandDto.eventCommandDto())
                .build();
    }

    public static PromptResultGetCommandDto from(PromptResultGetCommandDto promptResultGetCommandDto, EventCommandDto eventCommandDto) {
        return PromptResultGetCommandDto.builder()
                .gameCode(promptResultGetCommandDto.gameCode())
                .prompt(promptResultGetCommandDto.prompt())
                .eventCommandDto(eventCommandDto)
                .build();
    }

}
