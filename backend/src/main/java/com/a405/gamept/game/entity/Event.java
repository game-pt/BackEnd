package com.a405.gamept.game.entity;

import com.a405.gamept.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.List;

/**
 * Event
 *
 * 게임에서 발생할 Event를 정의.
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseEntity {
    /**
     * gameCode : pk
     */
    @Id
    private String code;

    /**
     * [fk] story : 이 이벤트가 사용되는 스토리
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_code")
    private Story story;

    /**
     * actList : 해당 이벤트에 연관되어 있는 행동들
     */
    @OneToMany(mappedBy = "event")
    @Comment("행동 리스트")
    private List<Act> actList;

    /**
     * name : 이벤트 이름
     */
    private String name;

    /**
     * prompt : ChatGpt 명령어
     */
    private String prompt;

    /**
     * item_yn : 아이템 획득 가능 여부
     */
    private char itemYn;

    /**
     * weight : 이벤트 랜덤 발생 가중치
     */
    @Column(columnDefinition = "TINYINT")
    private int weight;

}