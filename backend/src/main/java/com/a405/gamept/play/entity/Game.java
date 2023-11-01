package com.a405.gamept.play.entity;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "game", timeToLive = 14 * 24 * 60 * 60)
@Builder
@Getter
public class Game {

    /**
     * [pk] code : Game의 방 넘버
     */
    @NotBlank(message = "게임 코드가 올바르지 않습니다.")
    @Size(min = 6, max = 6, message = "게임 코드는 6자리여야 합니다.")
    @Id private String code;

    /**
     * storyCode : 이 게임의 스토리
     */
    @NotBlank(message = "스토리 코드가 올바르지 않습니다.")
    private String storyCode;

    /**
     * memory : 프롬포트 저장
     */
    private Prompt memory;

    /**
     * turn : 이 게임의 진행 상황
     */
    private int turn;

    /**
     * eventCnt : 이벤트가 얼마나 남았는지
     */
    private int eventCnt;

    /**
     * eventRate : 이벤트 가중치
     */
    private int eventRate;

    /**
     * playerList : 이 게임에 참여 중인 사람 목록
     */
    @Size(min = 0, max = 4, message = "게임 인원은 4명을 넘을 수 없습니다.")
    private List<Player> playerList;

    /**
     * promptList : 최근 prompt 5개
     */
    private List<Prompt> promptList;

    @Builder
    public Game(String code, String storyCode, Prompt memory, int turn, int eventCnt, int eventRate, List<Player> playerList, List<Prompt> promptList) {
        this.code = code;
        this.storyCode = storyCode;
        this.memory = memory;
        this.turn = turn;
        this.eventCnt = eventCnt;
        this.eventRate = eventRate;
        this.playerList = playerList;
        this.promptList = promptList;
    }

    @Builder
    public Game(String code, String storyCode) {
        this.code = code;
        this.storyCode = storyCode;
        this.turn = 0;
        this.eventCnt = 0;
        this.eventRate = 0;
    }
}
