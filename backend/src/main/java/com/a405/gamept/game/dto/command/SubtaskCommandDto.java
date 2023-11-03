package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.dto.request.SubtaskRequestDto;
import com.a405.gamept.game.entity.Subtask;
import jakarta.validation.constraints.NotNull;

public record SubtaskCommandDto(
        @NotNull(message = "사용자 코드는 필수입니다.")
        String playerCode,
        @NotNull(message = "서브테스크 항목은 필수입니다.")
        Subtask subtask,
        @NotNull(message = "서브테스크 항목은 필수입니다.")
        String gameCode
) {

}
