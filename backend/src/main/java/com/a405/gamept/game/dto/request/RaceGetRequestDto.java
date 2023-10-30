package com.a405.gamept.game.dto.request;

import com.a405.gamept.game.dto.command.RaceGetCommandDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record RaceGetRequestDto(
        @NotBlank(message = "스토리가 입력되지 않았습니다.") /*@Size(7)*/ String storyCode
) { }
