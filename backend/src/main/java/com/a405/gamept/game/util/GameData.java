package com.a405.gamept.game.util;

import java.util.HashMap;
import java.util.Map;

public interface GameData {
    // 몬스터 레벨 확률
    Map<Integer, Integer> monsterRate = new HashMap<>() {{
        put(2, 5);  // 레벨 2 차이 확률 1
        put(1, 15);  // 레벨 1 차이 확률 2
        put(0, 60);  // 레벨 0 차이 확률 5
        put(-1, 15);  // 레벨 1 차이 확률 2
        put(-2, 5);  // 레벨 2 차이 확률 1
    }};
    double MAX_PERCENTAGE = 100.0;

    int PLAYER_MAX_LEVEL = 10;
    int MONSTER_MAX_LEVEL = 10;
    int MAX_STAT = 20;
    int MAX_ITEM_SIZE = 4;
    int FIRST_STEP = 8;
    int SECOND_STEP = 12;
    int THIRD_STEP = 16;
    int FIRST_BONUS = 1;
    int SECOND_BONUS = 2;
    int THIRD_BONUS = 3;
    int FIRST_DEMERIT = -1;
}
