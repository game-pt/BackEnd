package com.a405.gamept.game.dto.request;

import com.a405.gamept.game.dto.command.ActGetCommandDto;
import com.a405.gamept.game.entity.Subtask;
import jakarta.validation.constraints.NotBlank;

/**
 * 이벤트에 속하는 선택지 반환을 요청하기 위함
 * @param eventCode : 선택지 목록을 가르키는 이벤트
 * @author : 지환
 */
public record ActGetRequestDto(
        @NotBlank(message = "이벤트 코드는 필수입니다.")
        String eventCode,
        @NotBlank(message = "플레이어 코드는 필수입니다.")
        String playerCode
) {
    public ActGetCommandDto toCommand(String gameCode){
        return new ActGetCommandDto(this.eventCode, gameCode, this.playerCode);
    }
}
