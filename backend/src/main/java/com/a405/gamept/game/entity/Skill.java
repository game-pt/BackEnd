package com.a405.gamept.game.entity;

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
public class Skill {

    /**
     * [pk] code: Skill의 고유 코드
     */
    @Id
    private String code;
    /**
     * [fk] job_code: Job의 고유 코드
     */
    @ManyToOne
    @JoinColumn(name = "job_code")
    private Job job;
    /**
     * name: Skill의 이름
     */
    private String name;

    @Builder
    public Skill(String code, Job job, String name) {
        this.code = code;
        this.job = job;
        this.name = name;
    }

}
