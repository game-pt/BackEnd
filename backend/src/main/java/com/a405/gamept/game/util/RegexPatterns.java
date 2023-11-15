package com.a405.gamept.game.util;


import java.util.regex.Pattern;

public interface RegexPatterns {
    String GAME = "[a-zA-Z\\d]{6}$";
    String FIGHTING_ENEMY = "[a-zA-Z\\d]{6}$";
    String PLAYER = "^[a-zA-Z\\d]{6}-[a-zA-Z\\d]{6}$";
    String STORY = "^STORY-[0-9]{3}$";
    String STAT = "^STAT-[0-9]{3}$";
    String JOB = "^JOB-[0-9]{3}$";
    String RACE = "^RACE-[0-9]{3}$";
    String ITEM = "^ITEM-[0-9]{3}$";
    String MONSTER = "^MON-[0-9]{3}$";
}
