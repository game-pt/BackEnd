package com.a405.gamept.game.dto.request;

import com.a405.gamept.game.dto.command.ActResultGetCommandDto;
import com.a405.gamept.game.dto.command.FightResultGetCommandDto;
import com.a405.gamept.game.entity.Subtask;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 전투 진행 결과 요청
 * @param actCode : 플레이어가 선택한 행동 코드
 * @param playerCode : 해당 행동을 하는 플레이어 코드
 * @author : 지환
 */
public record FightResultGetRequestDto(
        @NotBlank(message = "행동 코드는 필수입니다.")
        String actCode,
        @NotNull(message = "하위 항목 여부는 필수입니다.")
        Subtask subtask,
        @NotBlank(message = "플레이어 코드는 필수입니다.")
        String playerCode,
        @NotBlank(message = "몬스터 코드는 필수입니다.")
        String gmonsterCode
) {
    public FightResultGetCommandDto toCommand(String gameCode){
        return new FightResultGetCommandDto(this.actCode, this.subtask, gameCode, this.playerCode, this.gmonsterCode);
    }
}
