package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.entity.Act;
import com.a405.gamept.game.entity.Event;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Builder
@Slf4j
public record EventCommandDto(
        @NotBlank(message = "이벤트 코드가 입력되지 않았습니다.") String eventCode,
        @NotBlank(message = "이벤트 이름이 입력되지 않았습니다.") String eventName,
        @NotBlank(message = "이벤트에 대한 행동이 입력되지 않았습니다.") List<ActCommandDto> acts
) {
    public static EventCommandDto from(Event event, List<ActCommandDto> acts) {
        return EventCommandDto.builder()
                .eventCode(event.getCode())
                .eventName(event.getName())
                .acts(acts)
                .build();
    }
}