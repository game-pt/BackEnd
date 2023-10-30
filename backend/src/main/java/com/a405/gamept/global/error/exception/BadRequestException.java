package com.a405.gamept.global.error.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends Exception {
    private HttpStatus status = HttpStatus.BAD_REQUEST;
    public BadRequestException () {
        super("알 수 없는 오류");
    }
    public BadRequestException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return this.status;
    }

}
