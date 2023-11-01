package com.a405.gamept.game.dto.command;

import jakarta.validation.constraints.NotBlank;

/**
 * 이벤트에 속한 선택지를 반환하기 위해 내부 로직에서 사용하는 record
 * @param eventCode : 선택지 목록을 가르키는 이벤트
 * @author : 지환
 */
public record ActGetCommandDto(
        @NotBlank(message = "이벤트 코드는 필수입니다.")
        String eventCode,
        @NotBlank(message = "게임 코드는 필수입니다.")
        String gameCode,
        @NotBlank(message = "플레이어 코드는 필수입니다.")
        String playerCode
) {

}
