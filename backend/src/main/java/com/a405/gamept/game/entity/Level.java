package com.a405.gamept.game.entity;

import com.a405.gamept.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Level
 *
 * 플레이어가 레벨 업하기 위한 필요 경험치를 정의.
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Level extends BaseEntity {

    /**
     * gameCode : pk
     */
    @Id
    private String code;

    /**
     * level : 해당 레벨
     */
    private int level;
    
    /**
     * exp : 필요 경험치
     */
    @Column(columnDefinition = "TINYINT")
    private int exp;

    /**
     * [fk] story : 이 레벨이 적용되는 스토리
     * */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id")
    private Story story;
}
