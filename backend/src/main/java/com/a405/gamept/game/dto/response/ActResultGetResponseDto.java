package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.dto.command.EventCommandDto;
import com.a405.gamept.game.dto.command.PromptResultGetCommandDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record ActResultGetResponseDto(
        @NotBlank(message = "게임 코드가 입력되지 않았습니다.") String gameCode,
        @NotBlank(message = "프롬프트가 입력되지 않았습니다.") String prompt,
        String itemYn,
        String gameOverYn
) {
    public static ActResultGetResponseDto of(String gameCode, String prompt, String itemYn, String gameOverYn) {
        return ActResultGetResponseDto.builder()
                .gameCode(gameCode)
                .prompt(prompt)
                .itemYn(itemYn)
                .gameOverYn(gameOverYn)
                .build();
    }
}
