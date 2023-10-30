package com.a405.gamept.global.error.exception.custom;

import com.a405.gamept.global.error.enums.ErrorMessage;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(ErrorMessage errorMessage) {
        super(errorMessage.getPhrase());
        this.code = errorMessage.getCode();
    }
}