package com.a405.gamept.member.util.exception;

import com.a405.gamept.member.util.enums.MemberErrorMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MemberException extends RuntimeException {
    private final HttpStatus code;

    public MemberException(MemberErrorMessage memberErrorMessage) {
        super(memberErrorMessage.getMessage());
        this.code = memberErrorMessage.getCode();
    }

    public MemberException(String message) {
        super(message);
        this.code = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
