package com.a405.gamept.game.util.exception;

import com.a405.gamept.global.error.exception.InternalServerException;

public class GameInvalidException extends InternalServerException {
    public GameInvalidException() {
        super("게임이 유효하지 않습니다.");
    }
}
