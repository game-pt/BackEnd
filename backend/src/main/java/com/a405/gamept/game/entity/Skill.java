package com.a405.gamept.game.entity;

import com.a405.gamept.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Skill
 *
 * 해당 직업이 사용할 수 있는 스킬을 정의.
 */
@Entity
@Table(name = "skill")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Skill extends BaseEntity {

    /**
     * [pk] gameCode: Skill의 고유 코드
     */
    @Id
    private String code;
    /**
     * [fk] job_code: 직업의 고유 코드
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_code")
    private Job job;
    /**
     * [fk] stat_code: 주사위에 보너스 점수를 줄 스탯의 고유 코드
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stat_code")
    private Stat stat;
    /**
     * name: Skill의 이름
     */
    private String name;
    /**
     * desc: Skill의 설명
     */
    private String desc;
    /**
     * success_std: 스킬의 성공 판정을 가리기 위한 기준치
     */
    @Column(columnDefinition = "TINYINT")
    private int successStd;
    /**
     * extremeStd: 스킬의 대성공, 대실패 판정을 가리기 위한 기준치
     */
    @Column(columnDefinition = "TINYINT(10)")
    private int extremeStd;

    @Builder
    public Skill(String code, Job job, String name, String desc, int success_std, int extreme_std) {
        this.code = code;
        this.job = job;
        this.name = name;
        this.desc = desc;
        this.successStd = success_std;
        this.extremeStd = extreme_std;
    }

}
