package com.a405.gamept.play.entity;

import com.a405.gamept.game.entity.Item;
import java.util.List;
import java.util.Map;

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
     * level : 사용자의 레벨
     */
    private int level;

    /**
     * hp : 사용자의 체력
     */
    private int hp;

    /**
     * exp : 사용자의 경험치 MAX = 10
     */
    private int exp;
    /**
     * stat : 사용자의 스탯 리스트
     */
    private Map<String, Integer> stat;

    /**
     * tmpAddStat : 아이템 사용으로 인해 추가된 변수
     * 게임이 끝나면 원래 없어져야함
     */
    private Map<String, Integer> tmpAddStat;

    /**
     * statPoint : 스탯을 올릴 수 있는 포인트
     */
    private int statPoint;
    
    /**
     * itemList : 사용자가 가지고 있는 아이템 목록
     */
    private List<String> itemCodeList;

    /**
     * newItem : 새로운 아이템, 사용자가 획득을 원할 시 습득할 아이템이다.
     */
    private String newItemCode;
    
    /**
     * nickname : 사용자의 닉네임
     */
    private String nickname;
}
