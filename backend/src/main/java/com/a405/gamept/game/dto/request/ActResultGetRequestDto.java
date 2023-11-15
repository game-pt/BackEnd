package com.a405.gamept.game.dto.request;

import com.a405.gamept.game.dto.command.ActGetCommandDto;
import com.a405.gamept.game.dto.command.ActResultGetCommandDto;
import jakarta.validation.constraints.NotBlank;

/**
 * 선택한 행동에 따른 결과 요청
 * @param actCode : 플레이어가 선택한 행동 코드
 * @param playerCode : 해당 행동을 하는 플레이어 코드
 * @author : 지환
 */
public record ActResultGetRequestDto(
        @NotBlank(message = "행동 코드는 필수입니다.")
        String actCode,
        @NotBlank(message = "플레이어 코드는 필수입니다.")
        String playerCode
) {
    public ActResultGetCommandDto toCommand(String gameCode){
        return new ActResultGetCommandDto(this.actCode, gameCode, this.playerCode);
    }
}
