package com.a405.gamept.play.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.a405.gamept.game.entity.Job;
import com.a405.gamept.game.entity.Race;
import com.a405.gamept.game.entity.Stat;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "player", timeToLive = 14 * 24 * 60 * 60)
@Builder
@Getter
public class Player {

    /**
     * [pk] code : 사용자 구분 값
     */
    @Id private String code;

    /**
     * raceCode : 사용자의 종족 코드
     */
    private Race race;

    /**
     * jobCode : 사용자의 직업 코드
     */
    private Job job;

    /**
     * stat : 사용자의 스탯
     */
    private Map<String, Integer> stat;

    /**
     * nickname : 사용자의 닉네임
     */
    private String nickname;
}
