package com.a405.gamept.global.error.enums;

import static org.springframework.http.HttpStatus.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    INTERVAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, "요청을 처리하는 과정에서 서버가 예상하지 못한 오류가 발생하였습니다."),
    USER_NOT_FOUND(NOT_FOUND, "해당 회원을 찾을 수 없습니다."),
    INVALID_BOARD_REQUEST(BAD_REQUEST, "해당 게시물 요청이 적절하지 않습니다."),
    DUPLICATE_ALGORITHM_REQUEST(CONFLICT, "해당 날짜에 이미 알고리즘이 존재합니다."),
    INVALID_DICE_RESPONSE(BAD_REQUEST, "다이스의 값이 1~6사이의 값이 아닙니다. 다시 시도해주세요"),
    DICE_NOT_FOUND(NOT_FOUND, "다이스의 값을 찾을 수 없습니다. 다시 시도해주세요"),
    GAME_NOT_FOUND(NOT_FOUND, "해당 게임을 찾을 수 없습니다."),
    PLAYER_NOT_FOUND(NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    INVALID_GAME_REQUEST(BAD_REQUEST, "해당 유저의 게임이 아닙니다.");

    private final int code;
    private final String phrase;

    ErrorMessage(HttpStatus code, String phrase) {
        this.code = code.value();
        this.phrase = phrase;
    }
}
