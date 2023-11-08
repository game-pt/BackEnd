package com.a405.gamept.game.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record ItemSetResponseDto(
        @NotBlank(message = "아이템 코드가 존재하지 않습니다.")
        @Pattern(regexp = "^ITEM-([0-9]{3})$", message = "아이템 코드가 올바르지 않습니다.")
        String itemCode,
        @NotBlank(message = "플레이어 코드가 존재하지 않습니다.")
        String playerCode,
        @Size(max=4, message = "아이템 소지 횟수는 4개를 넘을 수 없습니다.")
        List<ItemGetResponseDto> itemList
) { }
