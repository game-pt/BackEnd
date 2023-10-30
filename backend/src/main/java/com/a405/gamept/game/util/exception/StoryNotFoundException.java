package com.a405.gamept.game.util.exception;

import com.a405.gamept.global.error.exception.BadRequestException;

public class StoryNotFoundException extends BadRequestException {
    public StoryNotFoundException() {
        super("스토리가 존재하지 않습니다.");
    }
}
