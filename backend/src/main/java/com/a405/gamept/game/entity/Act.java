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
public class Act extends BaseEntity {
    /**
     * code : pk
     * */
    @Id
    private String code;

    /**
     * name : 행동명
     * */
    private String name;

    /**
     * success_std : 성공 기준치
     * */
    private int success_std;

    /**
     * extreme_std : 대실패, 실패, 성공, 대성공 차이 기준치
     * */
    private int extreme_std;

    /**
     * event : 이 행동이 사용되는 이벤트
     * */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    /**
     * stat : 이 행동에 영향을 주는 스탯
     * */
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "stat_id")
//    private Stat stat;
}
