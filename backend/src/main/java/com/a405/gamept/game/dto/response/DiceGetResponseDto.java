package com.a405.gamept.game.dto.response;

import com.a405.gamept.global.error.enums.ErrorMessage;
import com.a405.gamept.global.error.exception.custom.BusinessException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.NotFound;

public record DiceGetResponseDto(
        @NotNull(message = "다이스 값이 들어있지 않습니다. 다시 돌려주세요.")
        @Size(min = 1, max = 6, message = "다이스는 1에서 6사이의 값을 가집니다.")
        int dice1,
        @NotNull(message = "다이스 값이 들어있지 않습니다. 다시 돌려주세요.")
        @Size(min = 1, max = 6, message = "다이스는 1에서 6사이의 값을 가집니다.")
        int dice2
) {
    public static DiceGetResponseDto of(int dice1, int dice2){
        return new DiceGetResponseDto(dice1, dice2);
    }
}
