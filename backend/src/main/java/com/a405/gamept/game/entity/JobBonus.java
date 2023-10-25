package com.a405.gamept.game.entity;

import jakarta.persistence.*;

/**
 * JobBonus
 *
 * 직업 별 추가 스탯을 정의.
 */
@Entity
@Table(name = "job_bonus")
public class JobBonus {

    /**
     * [pk] code: 직업 별 Bonus Stat의 고유 코드
     */
    @Id
    private String code;
    /**
     * [fk] job_code: 직업의 고유 코드
     */
    @ManyToOne
    @JoinColumn(name = "job_code")
    private Job job;
    /**
     * [fk] stat_code: 직업에 따라 추가로 획득할 스탯의 고유 코드
     */
    @ManyToOne
    @JoinColumn(name = "stat_code")
    private Stat stat;
    /**
     * stat_bonus: 추가로 획득할 스탯의 양
     */
    private short stat_bonus;

}
