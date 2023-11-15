package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.entity.Item;
import com.a405.gamept.game.util.RegexPatterns;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ItemGetResponseDto(
        @NotBlank(message = "아이템 코드가 존재하지 않습니다.")
        @Pattern(regexp = RegexPatterns.ITEM, message = "아이템 코드가 올바르지 않습니다.")
        String code,
        @NotBlank(message = "아이템명이 존재하지 않습니다.") String name,
        @NotBlank(message = "아이템 설명이 존재하지 않습니다.") String desc,
        @Positive(message = "아이템의 무게는 양수여야 합니다.")
        @Max(value = 20, message = "아이템의 무게는 20을 넘을 수 없습니다.")
        int weight
) {
        public static ItemGetResponseDto from(Item item) {
                return ItemGetResponseDto.builder()
                        .code(item.getCode())
                        .name(item.getName())
                        .desc(item.getDesc())
                        .weight(item.getWeight())
                        .build();
        }
}
