package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.entity.Act;
import com.a405.gamept.game.entity.Subtask;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record ActCommandDto(
        @NotBlank(message = "행동 코드가 입력되지 않았습니다.") String actCode,
        @NotBlank(message = "행동 이름이 입력되지 않았습니다.") String actName,
        @NotBlank(message = "하위 항목이 입력되지 않았습니다.") Subtask subtask
) {
    public static ActCommandDto from(Act act) {
        return ActCommandDto.builder()
                .actCode(act.getCode())
                .actName(act.getName())
                .subtask(act.getSubtask())
                .build();
    }
}
