package com.a405.gamept.game.entity;

import com.a405.gamept.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Job
 *
 * 플레이어가 선택할 수 있는 직업을 정의.
 */
@Entity
@Table(name = "job")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Job extends BaseEntity {

    /**
     * [pk] code: Job의 고유 코드
     */
    @Id
    private String code;

    /**
     * [fk] story_code: 해당 직업이 있는 Story의 고유 코드
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_code")
    private Story story;

    /**
     * name: Job의 이름
     */
    private String name;

    @Builder
    public Job(String code, Story story, String name) {
        this.code = code;
        this.story = story;
        this.name = name;
    }

}