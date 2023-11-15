package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.entity.Act;
import com.a405.gamept.game.entity.Subtask;
import jakarta.validation.constraints.NotBlank;

/**
 * 이벤트에 해당하는 선택지 반환
 * @param actCode : 선택지 대분류
 * @param actName : 선택지 대분류 명칭
 * @author : 지환
 */
public record ActGetResponseDto(
        @NotBlank(message = "행동 코드는 필수입니다.")
        String actCode,
        @NotBlank(message = "행동 명칭은 필수 입니다.")
        String actName,

        Subtask subtask
) {
        public static ActGetResponseDto of(Act act){
                return new ActGetResponseDto(act.getCode(), act.getName(), act.getSubtask());
        }
}
