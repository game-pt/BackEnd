package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.dto.request.ChatRequestDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ChatCommandDto(
        @NotBlank(message = "게임코드가 올바르지 않습니다.")
        String gameCode,
        @NotBlank(message = "플레이어 코드가 올바르지 않습니다.")
        String playerCode,
        @NotBlank(message = "메시지 내용이 올바르지 않습니다.")
        String message
) {
    public static ChatCommandDto from(String gameCode, ChatRequestDto chatRequestDto) {
        return ChatCommandDto.builder()
                .gameCode(gameCode)
                .playerCode(chatRequestDto.playerCode())
                .message(chatRequestDto.message())
                .build();
    }
}
