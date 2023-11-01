package com.a405.gamept.game.dto.request;

import lombok.*;
import lombok.extern.slf4j.Slf4j;


@Builder(access = AccessLevel.PRIVATE)
@Slf4j
public record FindEventByStoryCodeRequestDto(
        String storyCode
) { }
