package com.a405.gamept.global.error.exception;

import org.springframework.http.HttpStatus;

public class InternalServerException extends Exception {
    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    public InternalServerException() {
        super("알 수 없는 오류");
    }
    public InternalServerException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return this.status;
    }

}
