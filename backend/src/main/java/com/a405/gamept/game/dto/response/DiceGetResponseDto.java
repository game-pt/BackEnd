package com.a405.gamept.game.dto.response;

import com.a405.gamept.global.error.enums.ErrorMessage;
import com.a405.gamept.global.error.exception.custom.BusinessException;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.NotFound;

public record DiceGetResponseDto(
        @NotNull(message = "다이스 값이 들어있지 않습니다. 다시 돌려주세요.")
        int dice1,
        @NotNull(message = "다이스 값이 들어있지 않습니다. 다시 돌려주세요.")
        int dice2
) {
    public static DiceGetResponseDto of(int dice1, int dice2){
        if(dice1 < 1 || dice1 > 6){
            throw new BusinessException(ErrorMessage.INVALID_DICE_RESPONSE);
        }
        if(dice2 < 1 || dice2 > 6){
            throw new BusinessException(ErrorMessage.INVALID_DICE_RESPONSE);
        }

        return new DiceGetResponseDto(
                dice1,
                dice2
        );
    }
}
