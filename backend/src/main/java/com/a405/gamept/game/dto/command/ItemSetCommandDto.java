package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.dto.request.ItemSetRequestDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ItemSetCommandDto (
        @NotBlank(message = "게임이 존재하지 않습니다.") String gameCode,
        @NotBlank(message = "플레이어가 존재하지 않습니다.") String playerCode
) {
    public static ItemSetCommandDto from(ItemSetRequestDto itemSetRequestDto) {
        return ItemSetCommandDto.builder()
                .gameCode(itemSetRequestDto.gameCode())
                .playerCode(itemSetRequestDto.playerCode())
                .build();
    }
}
