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
        @NotBlank(message = "행동 코드가 입력되지 않았습니다.") String code,
        @NotBlank(message = "행동 이름이 입력되지 않았습니다.") String name,
        @NotBlank(message = "하위 항목이 입력되지 않았습니다.") Subtask subtask
) {
    public static ActCommandDto from(Act act) {
        return ActCommandDto.builder()
                .code(act.getCode())
                .name(act.getName())
                .subtask(act.getSubtask())
                .build();
    }
}
