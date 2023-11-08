package com.a405.gamept.game.util.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum GameErrorMessage  {
    GAME_NOT_FOUND(BAD_REQUEST, "게임이 존재하지 않습니다."),
    STORY_NOT_FOUND(BAD_REQUEST, "스토리가 존재하지 않습니다."),
    EVENT_NOT_FOUND(BAD_REQUEST, "이벤트가 존재하지 않습니다."),
    MONSTER_INVALID(INTERNAL_SERVER_ERROR, "몬스터가 유효하지 않습니다."),
    RACE_NOT_FOUND(BAD_REQUEST, "종족이 존재하지 않습니다."),
    RACE_INVALID(INTERNAL_SERVER_ERROR, "종족이 유효하지 않습니다."),
    JOB_NOT_FOUND(BAD_REQUEST, "직업이 존재하지 않습니다."),
    JOB_INVALID(INTERNAL_SERVER_ERROR, "직업이 유효하지 않습니다."),
    STAT_INVALID(INTERNAL_SERVER_ERROR, "스탯이 유효하지 않습니다."),
    INVALID_DICE_RESPONSE(BAD_REQUEST, "다이스의 값이 1~6사이의 값이 아닙니다. 다시 시도해주세요"),
    DICE_NOT_FOUND(NOT_FOUND, "다이스의 값을 찾을 수 없습니다. 다시 시도해주세요"),
    PLAYER_NOT_FOUND(NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    PLAYER_FULL(BAD_REQUEST, "게임의 플레이어 정원이 다 찼습니다."),
    INVALID_GAME_REQUEST(BAD_REQUEST, "해당 유저의 게임이 아닙니다."),
    INVALID_ACT_REQUEST(BAD_REQUEST, "해당 이벤트에 관여된 행동을 찾을 수 없습니다."),
    ACT_NOT_FOUND(BAD_REQUEST, "해당 동작이 존재하지 않습니다."),
    INVALID_ITEM_REQUEST(BAD_REQUEST, "해당 스토리에는 아이템이 없습니다."),
    //스킬 관련 에러 메세지
    SKILL_NOT_FOUND(BAD_REQUEST, "해당 스킬이 존재하지 않습니다.");

    private final HttpStatus code;
    private final String message;
}
