package com.a405.gamept.game.dto.command;

import com.a405.gamept.game.dto.request.JobGetRequestDto;
import com.a405.gamept.play.entity.Game;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record JobGetCommandDto(
        @NotBlank(message = "게임이 존재하지 않습니다.") String gameCode
) {
    public static JobGetCommandDto from(JobGetRequestDto jobGetRequestDto) {
        return JobGetCommandDto.builder()
                .gameCode(jobGetRequestDto.gameCode())
                .build();
    }
}
