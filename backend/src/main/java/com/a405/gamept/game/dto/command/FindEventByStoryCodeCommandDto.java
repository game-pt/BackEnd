package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.dto.request.FindEventByStoryCodeRequestDto;
import com.a405.gamept.game.dto.request.GetPromptResultRequestDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record FindEventByStoryCodeCommandDto(
        @NotBlank(message = "게임 코드가 입력되지 않았습니다.") String code
) {
    public static FindEventByStoryCodeCommandDto from(FindEventByStoryCodeRequestDto findEventByStoryCodeRequestDto) {
        return FindEventByStoryCodeCommandDto.builder()
                .code(findEventByStoryCodeRequestDto.code())
                .build();
    }
}
