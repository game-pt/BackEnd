package com.a405.gamept.game.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Item Entity
 *
 * 아이템의 이름과 설명, 무게 등을 정의.
 */
@Entity
@Table(name = "item")
@NoArgsConstructor
@Getter
public class Item {

    @Id
    private String code;
    @ManyToOne
    @JoinColumn(name = "story_code")
    private Story story;
    private String name;
    private String desc;
    private String img;
    private Short weight;

    @Builder
    public Item(String code, Story story, String name, String desc, String img, short weight) {
        this.code = code;
        this.story = story;
        this.name = name;
        this.desc = desc;
        this.img = img;
        this.weight = weight;
    }

}
