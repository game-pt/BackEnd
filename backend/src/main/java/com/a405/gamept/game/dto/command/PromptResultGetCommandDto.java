package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.dto.request.PromptResultGetRequestDto;
import com.a405.gamept.game.util.RegexPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record PromptResultGetCommandDto(

        @NotBlank(message = "게임이 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.GAME, message = "게임이 올바르지 않습니다.")
        String gameCode,
        @NotBlank(message = "플레이어가 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.PLAYER, message = "플레이어가 올바르지 않습니다.")
        String playerCode,
        @NotBlank(message = "프롬프트가 입력되지 않았습니다.")
        String prompt,
        EventCommandDto eventCommandDto
) {
    public static PromptResultGetCommandDto from(PromptResultGetRequestDto promptResultGetRequestDto, String gameCode) {
        return PromptResultGetCommandDto.builder()
                .gameCode(gameCode)
                .playerCode(promptResultGetRequestDto.playerCode())
                .prompt(promptResultGetRequestDto.prompt())
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
