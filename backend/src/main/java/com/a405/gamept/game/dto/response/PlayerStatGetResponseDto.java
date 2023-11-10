package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.dto.command.PlayerStatGetCommandDto;
import com.a405.gamept.game.util.RegexPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record PlayerStatGetResponseDto(
        @NotBlank(message = "플레이어 코드가 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.PLAYER, message = "플레이어 코드가 올바르지 않습니다.")
        String playerCode,

        @NotNull(message = "플레이어의 스탯이 존재하지 않습니다.")
        List<StatGetResponseDto> statList
) {
    public static PlayerStatGetResponseDto from(PlayerStatGetCommandDto playerStatGetCommandDto, List<StatGetResponseDto> statList) {
        return PlayerStatGetResponseDto.builder()
                .playerCode(playerStatGetCommandDto.playerCode())
                .statList(statList)
                .build();
    }

    public static PlayerStatGetResponseDto of(String playerCode, List<StatGetResponseDto> statList) {
        return PlayerStatGetResponseDto.builder()
                .playerCode(playerCode)
                .statList(statList)
                .build();
    }
}
