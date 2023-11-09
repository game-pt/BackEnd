package com.a405.gamept.game.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemCase {
    STAT("STAT_UP"), DAMAGE("DAMAGE"), HEAL("HEAL"), ESCAPE("ESCAPE");
    private final String key;
}
