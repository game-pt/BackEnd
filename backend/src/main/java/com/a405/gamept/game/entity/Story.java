package com.a405.gamept.game.entity;

import com.a405.gamept.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Story extends BaseEntity {

    /**
    * code : pk
    * */
    @Id
    private String code;

    /**
     * name : 스토리 이름
     * */
    private String name;

    /**
     * desc : 스토리 설명
     * */
    private String desc;
}