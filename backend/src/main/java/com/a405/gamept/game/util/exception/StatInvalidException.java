package com.a405.gamept.game.util.exception;

import com.a405.gamept.global.error.exception.InternalServerException;

public class StatInvalidException extends InternalServerException {
    public StatInvalidException() {
        super("스탯이 유효하지 않습니다.");
    }
}
