package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.util.GameData;
import com.a405.gamept.game.util.RegexPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record ItemDeleteResponseDto(
        @NotBlank(message = "플레이어 코드가 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.PLAYER, message = "플레이어 코드가 올바르지 않습니다.")
        String playerCode,
        @Size(max=GameData.MAX_ITEM_SIZE, message = "아이템 소지 횟수는 " + GameData.MAX_ITEM_SIZE + "개를 넘을 수 없습니다.")
        List<ItemGetResponseDto> itemList
) { }
