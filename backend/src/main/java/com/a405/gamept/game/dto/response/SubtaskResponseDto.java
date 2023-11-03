package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.entity.Skill;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

public record SubtaskResponseDto(
        @NotNull(message = "코드는 필수입니다.")
        String code,
        @NotNull(message = "명칭은 필수입니다.")
        String name
) {
    public static SubtaskResponseDto of(String code, String name){
        return new SubtaskResponseDto(code, name);
    }
}
