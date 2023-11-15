package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.dto.command.EventCommandDto;
import com.a405.gamept.game.dto.command.PromptResultGetCommandDto;
import com.a405.gamept.game.util.RegexPatterns;
import com.a405.gamept.play.entity.Game;
import com.a405.gamept.play.entity.Prompt;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Builder(toBuilder = true)
@Slf4j
public record PromptResultGetResponseDto(
        @NotBlank(message = "게임이 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.GAME, message = "게임이 올바르지 않습니다.")
        String gameCode,
        // @NotBlank(message = "프롬프트가 입력되지 않았습니다.") String prompt,
        String itemYn,
        EventCommandDto event,
        MonsterGetResponseDto monster
) {
    public static PromptResultGetResponseDto from(PromptResultGetCommandDto promptResultGetCommandDto, EventCommandDto eventCommandDto) {
        return PromptResultGetResponseDto.builder()
                .gameCode(promptResultGetCommandDto.gameCode())
                // .prompt(promptResultGetCommandDto.prompt())
                .event(eventCommandDto)
                .build();
    }

    // 몬스터 추가 로직
    public static PromptResultGetResponseDto of(PromptResultGetResponseDto promptResultGetResponseDto, MonsterGetResponseDto monster) {
        return promptResultGetResponseDto.toBuilder().monster(monster).build();
    }
}
