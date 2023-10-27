package com.a405.gamept.game.util.exception;


import com.a405.gamept.global.error.exception.InternalServerException;

public class RaceInvalidException extends InternalServerException {
    public RaceInvalidException() {
        super("종족이 유효하지 않습니다.");
    }
}
