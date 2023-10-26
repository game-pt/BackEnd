package com.a405.gamept.game.entity;

import com.a405.gamept.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Stat Entity
 *
 * Stat의 종류와 각 스탯의 이름을 정의.
 */
@Entity
@Table(name = "stat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Stat extends BaseEntity {

    /**
     * [pk] code: 스탯의 고유 코드
     */
    @Id
    private String code;
    /**
     * name: 스탯의 이름
     */
    private String name;

    @Builder
    public Stat(String code, String name) {
        this.code = code;
        this.name = name;
    }

}