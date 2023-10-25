package com.a405.gamept.game.entity;

import com.a405.gamept.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MonsterStat extends BaseEntity {
    /**
     * code : pk
     * */
    @Id
    private String code;

    /**
     * stat_value : 스탯 값
     * */
    private int statValue;

    /**
     * [fk] Monster : 어떤 몬스터인지
     * */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monster_id")
    private Monster Monster;

    /**
     * [fk] stat : 이 몬스터가 어떤 스탯을 가지는지
     * */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stat_id")
    private Stat stat;
}
