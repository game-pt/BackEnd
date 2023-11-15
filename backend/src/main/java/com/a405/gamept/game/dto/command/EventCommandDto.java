package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.dto.response.MonsterGetResponseDto;
import com.a405.gamept.game.entity.Act;
import com.a405.gamept.game.entity.Event;
import com.a405.gamept.game.util.RegexPatterns;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Builder
@Slf4j
public record EventCommandDto(
        @NotBlank(message = "이벤트 코드가 존재하지 않습니다.")
        String eventCode,
        @NotBlank(message = "이벤트명이 존재하지 않습니다.")
        String eventName,
        @Valid
        @NotBlank(message = "이벤트에 대한 행동이 입력되지 않았습니다.")
        @Size(min = 1, message = "이벤트에 대한 행동은 하나 이상이어야 합니다.")
        List<ActCommandDto> acts,
        @Valid
        MonsterGetResponseDto monster
) {
    public static EventCommandDto from(Event event, List<ActCommandDto> acts, MonsterGetResponseDto monster) {
        return EventCommandDto.builder()
                .eventCode(event.getCode())
                .eventName(event.getName())
                .acts(acts)
                .monster(monster)
                .build();
    }
}