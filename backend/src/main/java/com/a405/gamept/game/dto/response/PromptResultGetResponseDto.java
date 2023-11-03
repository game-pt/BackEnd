package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.dto.command.EventCommandDto;
import com.a405.gamept.game.dto.command.PromptResultGetCommandDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record PromptResultGetResponseDto(
        @NotBlank(message = "게임 코드가 입력되지 않았습니다.") String gameCode,
        @NotBlank(message = "프롬프트가 입력되지 않았습니다.") String prompt,
        EventCommandDto event
) {
    public static PromptResultGetResponseDto from(PromptResultGetCommandDto promptResultGetCommandDto, EventCommandDto eventCommandDto) {
        return PromptResultGetResponseDto.builder()
                .gameCode(promptResultGetCommandDto.gameCode())
                .prompt(promptResultGetCommandDto.prompt())
                .event(eventCommandDto)
                .build();
    }
}
