package com.a405.gamept.game.entity;

import com.a405.gamept.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.List;

/**
 * Monster
 *
 * 스토리에서 등장할 몬스터를 정의.
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Monster extends BaseEntity {
    /**
     * code : pk
     * */
    @Column(name = "code")
    @Comment("고유 코드")
    @Id private String code;

    /**
     * rank : 몬스터의 레벨
     * */
    @Column(name = "level", nullable = false, columnDefinition = "TINYINT", length=10)
    @Comment("몬스터의 레벨")
    private int level;

    /**
     * attack : 몬스터의 공격력
     * */
    @Column(name = "attack", nullable = false, columnDefinition = "TINYINT", length=100)
    @Comment("몬스터의 공격력")
    private int attack;

    /**
     * [fk] stat : 이 몬스터가 어떤 스토리에서 나오는지
     * */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_code")
    @Comment("스토리 코드")
    private Story story;
}
