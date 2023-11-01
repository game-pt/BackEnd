package com.a405.gamept.game.dto.response;

import com.a405.gamept.global.error.enums.ErrorMessage;
import com.a405.gamept.global.error.exception.custom.BusinessException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.NotFound;

public record DiceGetResponseDto(
        @NotNull(message = "다이스 값이 들어있지 않습니다. 다시 돌려주세요.")
        @Min(value = 1, message = "값은 최소 1 이어야 합니다")
        @Max(value = 10, message = "값은 최대 10 이어야 합니다")
        int dice1,
        @NotNull(message = "다이스 값이 들어있지 않습니다. 다시 돌려주세요.")
        @Min(value = 1, message = "값은 최소 1 이어야 합니다")
        @Max(value = 10, message = "값은 최대 10 이어야 합니다")
        int dice2,

        @NotNull(message = "다이스 값이 들어있지 않습니다. 다시 돌려주세요.")
        @Min(value = 1, message = "값은 최소 1 이어야 합니다")
        @Max(value = 10, message = "값은 최대 10 이어야 합니다")
        int dice3
) {
    public static DiceGetResponseDto of(int dice1, int dice2, int dice3){
        return new DiceGetResponseDto(dice1, dice2, dice3);
    }
}
