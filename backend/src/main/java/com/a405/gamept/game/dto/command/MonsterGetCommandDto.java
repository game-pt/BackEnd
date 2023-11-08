package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.util.GameData;
import com.a405.gamept.game.util.RegexPatterns;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record MonsterGetCommandDto(
        @NotBlank(message = "스토리가 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.STORY, message = "스토리가 올바르지 않습니다.")
        String storyCode,
        @Positive(message = "플레이어 레벨이 올바르지 않습니다.")
        @Max(value = GameData.PLAYER_MAX_LEVEL, message = "플레이어 레벨은 " + GameData.PLAYER_MAX_LEVEL + "을 넘을 수 없습니다.")
        int playerLevel
) { }
