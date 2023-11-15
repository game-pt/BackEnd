package com.a405.gamept.member.util.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@Getter
public enum MemberErrorMessage {
    URI_INVALID(INTERNAL_SERVER_ERROR, "URI가 유효하지 않습니다."),
    TOKEN_NOT_FOUND(BAD_REQUEST, "토큰이 존재하지 않습니다."),
    TOKEN_INVALID(INTERNAL_SERVER_ERROR, "토큰이 유효하지 않습니다.");
    
    private final HttpStatus code;
    private final String message;
}
