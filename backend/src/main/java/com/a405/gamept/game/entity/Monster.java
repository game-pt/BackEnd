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
public class Monster extends BaseEntity {
    /**
     * code : pk
     * */
    @Id
    private String code;

    /**
     * name : 몬스터 명
     * */
    private String name;

    /**
     * rank : 몬스터의 수준
     * */
    private int rank;

    /**
     * exp : 몬스터를 물리쳤을 때 얻는 경험치의 량
     * */
    private String exp;

    /**
     * [fk] stat : 이 몬스터가 어떤 스토리에서 나오는지
     * */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_code")
    private Story story;
}
