package com.a405.gamept.play.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "monster", timeToLive = 14 * 24 * 60 * 60)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Getter
@ToString
public class FightingEnermy {

    /**
     * [pk] code : 몬스터의 넘버
     */
    @NotBlank(message = "몬스터 코드가 올바르지 않습니다.")
    @Id private String code;

    /**
     * level : 몬스터의 잔량 HP
     */
    private int level;

    /**
     * hp : 몬스터의 잔량 HP
     */
    private int hp;

    /**
     * attack : 몬스터의 공격력
     */
    private int attack;

    @Builder
    public FightingEnermy(String code, int level, int hp, int attack){
        this.code = code;
        this.level = level;
        this.hp = hp;
        this.attack = attack;
    }

}
