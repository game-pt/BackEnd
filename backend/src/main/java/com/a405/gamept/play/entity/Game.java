package com.a405.gamept.play.entity;

import jakarta.persistence.Id;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "game")
@NoArgsConstructor
public class Game {

    /**
     * [pk] code : Game의 방 넘버
     */
    @Id
    private String code;

    /**
     * storyCode : 이 게임의 스토리
     */
    private String storyCode;

    /**
     * memory : 프롬포트 저장 
     */
    private Objects memory;

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
    private List<Player> playerList;

    /**
     * promptList : 최근 prompt 5개
     */
    private List<Prompt> promptList;
}
