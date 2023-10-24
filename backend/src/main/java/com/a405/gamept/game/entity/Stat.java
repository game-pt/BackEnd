package com.a405.gamept.game.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Stat {

    @Id
    private String id;
    private String name;

    @Builder
    public Stat(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
