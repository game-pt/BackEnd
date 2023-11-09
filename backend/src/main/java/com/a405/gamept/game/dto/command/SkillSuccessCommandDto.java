package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.entity.Act;
import com.a405.gamept.game.entity.Subtask;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record SkillSuccessCommandDto(
        boolean extremeFlag,
        boolean successFlag
) {
    public static SkillSuccessCommandDto of(boolean extremeFlag, boolean successFlag) {
        return new SkillSuccessCommandDto(extremeFlag, successFlag);
    }
}
