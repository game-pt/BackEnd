package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.dto.command.PromptResultGetCommandDto;
import com.a405.gamept.game.util.RegexPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(toBuilder = true)
@Slf4j
public record PromptResultGetResponseDto(
        @NotBlank(message = "게임이 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.GAME, message = "게임이 올바르지 않습니다.")
        String gameCode,
        String itemYn,
        EventGetResponseDto event,
        MonsterGetResponseDto monster
) {
    public static PromptResultGetResponseDto from(PromptResultGetCommandDto promptResultGetCommandDto, EventGetResponseDto eventGetResponseDto) {
        return PromptResultGetResponseDto.builder()
                .gameCode(promptResultGetCommandDto.gameCode())
                .event(eventGetResponseDto)
                .build();
    }

    // 몬스터 추가 로직
    public static PromptResultGetResponseDto of(PromptResultGetResponseDto promptResultGetResponseDto, MonsterGetResponseDto monster) {
        return promptResultGetResponseDto.toBuilder().monster(monster).build();
    }
}
