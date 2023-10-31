package com.a405.gamept.game.dto.request;

import com.a405.gamept.play.entity.Game;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record JobGetRequestDto(
        @NotBlank(message = "게임이 존재하지 않습니다.") String gameCode
) { }
