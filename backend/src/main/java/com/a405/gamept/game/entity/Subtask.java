package com.a405.gamept.game.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Subtask {
    NONE(""), SKILL("at_002"), ITEM("at_003");
    private final String key;
}
