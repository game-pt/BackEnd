package com.a405.gamept.game.util.exception;

import com.a405.gamept.global.error.exception.InternalServerException;

public class MonsterInvalidException extends InternalServerException {
    public MonsterInvalidException() {
        super("몬스터가 유효하지 않습니다.");
    }
}
