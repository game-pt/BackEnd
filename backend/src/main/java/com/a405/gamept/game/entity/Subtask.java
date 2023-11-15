package com.a405.gamept.game.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Subtask {
    NONE(""), SKILL("SKILL"), ITEM("ITEM");
    private final String key;
}
