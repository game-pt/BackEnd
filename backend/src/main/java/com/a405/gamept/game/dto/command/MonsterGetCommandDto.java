package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.util.FinalData;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record MonsterGetCommandDto(
        @NotBlank(message = "스토리가 입력되지 않았습니다.") String storyCode,
        @Positive(message = "플레이어 레벨은 양수여야 합니다.")
        @Max(value = FinalData.PLAYER_MAX_LEVEL, message = "플레이어 레벨은 " + FinalData.PLAYER_MAX_LEVEL + "을 넘을 수 없습니다.")
        int level
) { }
