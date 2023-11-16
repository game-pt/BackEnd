package com.a405.gamept.game.dto.response;

import com.a405.gamept.game.entity.Act;
import com.a405.gamept.game.entity.Event;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record EventGetResponseDto(
        @NotBlank(message = "이벤트 코드가 존재하지 않습니다.")
        String eventCode,
        @NotBlank(message = "이벤트명이 존재하지 않습니다.")
        String eventName,
        @Valid
        @NotBlank(message = "이벤트에 대한 행동이 입력되지 않았습니다.")
        @Size(min = 1, message = "이벤트에 대한 행동은 하나 이상이어야 합니다.")
        List<ActGetResponseDto> acts
) {
    public static EventGetResponseDto from(Event event) {
            List<ActGetResponseDto> actList = new ArrayList<>();
            for(Act act : event.getActList()) {
                    actList.add(ActGetResponseDto.of(act));
            }

            return EventGetResponseDto.builder()
                    .eventCode(event.getCode())
                    .eventName(event.getName())
                    .acts(actList)
                    .build();
    }
}
