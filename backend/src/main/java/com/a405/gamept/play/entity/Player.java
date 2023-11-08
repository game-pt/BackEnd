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
@Builder(toBuilder = true)
@Getter
@ToString
public class Player {

    /**
     * [pk] gameCode : 사용자 구분 값
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
     * hp : 사용자의 체력
     */
    private int hp;

    /**
     * exp : 사용자의 경험치 MAX = 10
     */
    private int exp;
    /**
     * stat : 사용자의 스탯
     */
    private Map<String, Integer> stat;

    /**
     * itemList : 사용자가 가지고 있는 아이템 목록
     */
    private List<Item> itemList;

    /**
     * newItem : 새로운 아이템, 사용자가 획득을 원할 시 습득할 아이템이다.
     */
    private Item newItem;
    
    /**
     * nickname : 사용자의 닉네임
     */
    private String nickname;
}
