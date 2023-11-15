package com.a405.gamept.game.dto.request;

import com.a405.gamept.game.dto.command.SubtaskCommandDto;
import com.a405.gamept.game.entity.Subtask;
import jakarta.validation.constraints.NotNull;

public record SubtaskRequestDto(
        @NotNull(message = "사용자 코드는 필수입니다.")
        String playerCode,
        @NotNull(message = "서브테스크 항목은 필수입니다.")
        Subtask subtask
) {
        public SubtaskCommandDto toCommand(String gameCode){
                return new SubtaskCommandDto(this.playerCode, this.subtask,gameCode);
        }
}
