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
     * name : 종족명
     * */
    private String name;

    /**
     * [fk] stat : 이 종족이 어떤 스토리에서 사용되는지
     * */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stat_id")
    private Stat stat;
}
