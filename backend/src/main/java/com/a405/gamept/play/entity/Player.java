package com.a405.gamept.play.entity;

import jakarta.persistence.Id;
import java.util.HashMap;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "player")
@NoArgsConstructor
public class Player {

    /**
     * [pk] code : 사용자 구분 값
     */
    @Id
    private String code;

    /**
     * raceCode : 사용자의 종족 코드
     */
    private String raceCode;

    /**
     * jobCode : 사용자의 직업 코드
     */
    private String jobCode;

    /**
     * stat : 사용자의 스탯
     */
    private HashMap<String, Integer> stat;

    /**
     * nickname : 사용자의 닉네임
     */
    private String nickname;
}
