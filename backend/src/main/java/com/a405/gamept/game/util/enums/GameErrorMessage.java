package com.a405.gamept.game.util.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum GameErrorMessage  {
    STORY_NOT_FOUND(BAD_REQUEST, "스토리가 존재하지 않습니다."),
    MONSTER_INVALID(INTERNAL_SERVER_ERROR, "몬스터가 유효하지 않습니다."),
    RACE_INVALID(INTERNAL_SERVER_ERROR, "종족이 유효하지 않습니다."),
    JOB_INVALID(INTERNAL_SERVER_ERROR, "직업이 유효하지 않습니다."),
    STAT_INVALID(INTERNAL_SERVER_ERROR, "스탯이 유효하지 않습니다.");

    private final HttpStatus code;
    private final String message;
}
