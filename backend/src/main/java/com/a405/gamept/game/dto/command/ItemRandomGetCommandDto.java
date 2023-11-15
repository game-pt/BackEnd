package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.dto.request.ItemSetRequestDto;
import com.a405.gamept.game.util.RegexPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ItemRandomGetCommandDto(
        String itemCode,
        String prompt
) {
    public static ItemRandomGetCommandDto of(String itemCode, String prompt) {
        return ItemRandomGetCommandDto.builder()
                .itemCode(itemCode)
                .prompt(prompt)
                .build();
    }
}
