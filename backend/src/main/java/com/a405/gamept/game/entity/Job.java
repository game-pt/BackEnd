package com.a405.gamept.game.entity;

import com.a405.gamept.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.List;

/**
 * Job
 *
 * 플레이어가 선택할 수 있는 직업을 정의.
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Job extends BaseEntity {

    /**
     * [pk] gameCode: Job의 고유 코드
     */
    @Comment("고유 코드")
    @Id private String code;

    /**
     * name: 직업명
     */
    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(10) CHARACTER SET UTF8")
    @Comment("직업명")
    private String name;

    /**
     * [fk] story_code: 해당 직업이 있는 Story의 고유 코드
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_code")
    @Comment("스토리 코드")
    private Story story;

    /**
     * [fk] stat_code: 해당 직업의 메인 스탯의 코드
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stat_code")
    @Comment("메인 스탯")
    private Stat stat;
    
    @OneToMany(mappedBy = "job")
    @Comment("직업 보너스 스탯 리스트")
    private List<JobBonus> jobBonusList;

    @Builder
    public Job(String code, Story story, String name) {
        this.code = code;
        this.story = story;
        this.name = name;
    }

}