package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.entity.Story;
import com.a405.gamept.play.entity.Game;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record GameSetResponseDto(
        @NotBlank(message = "게임 코드가 올바르지 않습니다.") String code
) {
    public static GameSetResponseDto from(Game game) {
        return GameSetResponseDto.builder()
                .code(game.getCode())
                .build();
    }
}
