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

    /**
     * [pk] code: Item의 고유한 코드
     */
    @Id
    private String code;
    /**
     * [fk] story_code: Story의 고유한 코드
     */
    @ManyToOne
    @JoinColumn(name = "story_code")
    private Story story;
    /**
     * name: Item의 이름
     */
    private String name;
    /**
     * desc: Item의 설명
     */
    private String desc;
    /**
     * img: 아이템의 이미지명
     */
    private String img;
    /**
     * weight: 아이템의 무게
     */
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