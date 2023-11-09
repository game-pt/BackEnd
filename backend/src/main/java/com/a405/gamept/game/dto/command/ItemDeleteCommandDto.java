package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.dto.request.ItemDeleteRequestDto;
import com.a405.gamept.game.util.RegexPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ItemDeleteCommandDto(
        @NotBlank(message = "게임이 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.GAME, message = "게임이 올바르지 않습니다.")
        String gameCode,
        @NotBlank(message = "플레이어가 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.PLAYER, message = "플레이어가 올바르지 않습니다.")
        String playerCode,
        @NotBlank(message = "아이템이 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.ITEM, message = "아이템이 올바르지 않습니다.")
        String itemCode
) {
    public static ItemDeleteCommandDto from(ItemDeleteRequestDto itemDeleteRequestDto, String itemCode) {
        return ItemDeleteCommandDto.builder()
                .gameCode(itemDeleteRequestDto.gameCode())
                .playerCode(itemDeleteRequestDto.playerCode())
                .itemCode(itemCode)
                .build();
    }
}
