package com.a405.gamept.game.dto.command;

import jakarta.validation.constraints.NotBlank;

/**
 * 선택지 결과물을 반환하기 위해 내부 로직에서 사용하는 record
 * @param actCode : 플레이어가 선택한 행동 코드
 * @param gameCode : 플레이어가 하는 게임 코드
 * @param playerCode : 해당 행동을 하는 플레이어 코드
 * @author : 지환
 */
public record ActResultGetCommandDto(
        @NotBlank(message = "이벤트 코드는 필수입니다.")
        String actCode,
        @NotBlank(message = "게임 코드는 필수입니다.")
        String gameCode,
        @NotBlank(message = "플레이어 코드는 필수입니다.")
        String playerCode
) {

}
