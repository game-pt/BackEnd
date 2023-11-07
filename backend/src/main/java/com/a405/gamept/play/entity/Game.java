package com.a405.gamept.play.entity;

import java.util.LinkedList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "game", timeToLive = 14 * 24 * 60 * 60)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Getter
@ToString
public class Game {

    /**
     * [pk] gameCode : Game의 방 넘버
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
     * memory : 프롬프트 저장
     */
    @Builder.Default
    private String memory = "";

    /**
     * turn : 이 게임의 진행 상황
     */
    @Builder.Default
    private int turn = 0;

    /**
     * eventCnt : 이벤트가 얼마나 남았는지
     */
    @Builder.Default
    private int eventCnt = 0;

    /**
     * eventRate : 이벤트 가중치
     */
    @Builder.Default
    private double eventRate = 0;

    /**
     * playerList : 이 게임에 참여 중인 사람 목록
     */
    @Size(max = 4, message = "게임 인원은 4명을 넘을 수 없습니다.")
    private List<String> playerList;

    /**
     * promptList : 최근 prompt 5개
     */
    @Builder.Default
    private List<Prompt> promptList = new LinkedList<>();

    @Builder
    public Game(String code, String storyCode, String memory, int turn, int eventCnt, double eventRate, List<String> playerList, List<Prompt> promptList) {
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
    }
}
