package com.a405.gamept.room.util.exception;

import com.a405.gamept.global.error.exception.BadRequestException;
import org.springframework.http.HttpStatus;

public class RoomNotFoundException extends BadRequestException {
    public RoomNotFoundException() {
        super("방이 존재하지 않습니다.");
    }
}
