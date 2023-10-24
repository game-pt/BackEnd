package com.a405.gamept.game.entity;

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

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {
    /**
     * code : pk
     * */
    @Id
    private String code;

    /**
     * name : 이벤트 이름
     * */
    private String name;

    /**
     * prompt : ChatGpt 명령어
     * */
    private String prompt;

    /**
     * story : 이 이벤트가 사용되는 스토리
     * */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id")
    private Story story;
}
