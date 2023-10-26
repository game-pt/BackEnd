package com.a405.gamept.game.entity;

import com.a405.gamept.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ActStat
 *
 * 행동에서 대성공 시 증가, 대실패 시 감소하는 스탯을 정의.
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActStat extends BaseEntity {
    /**
     * code : pk
     * */
    @Id
    private String code;

    /**
     * statBonus : 스탯 증감량
     * */
    @Column(columnDefinition = "TINYINT")
    private int statBonus;

    /**
     * [fk] act : 이 행동이 어떤 행동인지
     * */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "act_code")
    private Act act;

    /**
     * [fk] stat : 이 행동이 영향을 주는 스탯
     * */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stat_code")
    private Stat stat;
}