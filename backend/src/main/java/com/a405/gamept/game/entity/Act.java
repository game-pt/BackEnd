package com.a405.gamept.game.entity;

import com.a405.gamept.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Act
 *
 * 이벤트에서 선택할 행동을 정의.
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Act extends BaseEntity {
    /**
     * gameCode : pk
     * */
    @Id
    private String code;

    /**
     * name : 행동명
     * */
    private String name;

    /**
     * successStd : 성공 기준치
     * */
    @Column(columnDefinition = "TINYINT(10)")
    private int successStd;

    /**
     * extremeStd : 대실패, 실패, 성공, 대성공 차이 기준치
     * */
    @Column(columnDefinition = "TINYINT(10)")
    private int extremeStd;

    /**
     * subtask_yn : 하위 항목 존재 여부
     */
    @Enumerated(EnumType.STRING)
    private Subtask subtask;

    /**
     * [fk] event : 이 행동이 사용되는 이벤트
     * */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_code")
    private Event event;

    /**
     * [fk] stat : 이 행동에 영향을 주는 스탯
     * */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stat_code")
    private Stat stat;
}