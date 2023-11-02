package com.a405.gamept.game.entity;

import com.a405.gamept.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.List;

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
     * eventList : 스토리와 연관되어 있는 이벤트들
     */
    @OneToMany(mappedBy = "story")
    @Comment("이벤트 리스트")
    private List<Event> eventList;

    /**
     * name : 스토리 이름
     * */
    private String name;

    /**
     * desc : 스토리 설명
     * */
    private String desc;

}