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
public class RaceStat extends BaseEntity {
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
     * [fk] race : 종족이 무엇인지
     * */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "race_code")
    private Race race;

    /**
     * [fk] stat : 이 종족이 어떤 스탯을 가지는지
     * */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stat_code")
    private Stat stat;
}
