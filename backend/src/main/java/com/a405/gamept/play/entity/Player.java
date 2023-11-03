package com.a405.gamept.play.entity;

import com.a405.gamept.game.entity.Item;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.a405.gamept.game.entity.Job;
import com.a405.gamept.game.entity.Race;
import com.a405.gamept.game.entity.Stat;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "player", timeToLive = 14 * 24 * 60 * 60)
@Builder
@Getter
@ToString
public class Player {

    /**
     * [pk] code : 사용자 구분 값
     */
    @Id private String code;

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
    private Map<String, Integer> stat;

    /**
     * itemList : 사용자가 가지고 있는 아이템 목록
     */
    private List<Item> itemList; 
    
    /**
     * nickname : 사용자의 닉네임
     */
    private String nickname;
}
