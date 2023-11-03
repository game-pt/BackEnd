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
     * gameCode : pk
     * */
    @Column(name = "gameCode")
    @Comment("고유 코드")
    @Id private String code;

    /**
     * name : 몬스터 명
     * */
    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(10) CHARACTER SET UTF8")
    @Comment("몬스터명")
    private String name;

    /**
     * rank : 몬스터의 레벨
     * */
    @Column(name = "level", nullable = false, columnDefinition = "TINYINT", length=10)
    @Comment("몬스터의 레벨")
    private int level;

    /**
     * exp : 몬스터를 물리쳤을 때 얻는 경험치의 량
     * */
    @Column(name = "exp", nullable = false, columnDefinition = "TINYINT", length=100)
    @Comment("몬스터를 물리쳤을 때 얻는 경험치의 양")
    private int exp;

    @OneToMany(mappedBy = "monster")
    @Comment("몬스터 스탯 리스트")
    private List<MonsterStat> monsterStatList;

    /**
     * [fk] stat : 이 몬스터가 어떤 스토리에서 나오는지
     * */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_code")
    @Comment("스토리 코드")
    private Story story;
}
