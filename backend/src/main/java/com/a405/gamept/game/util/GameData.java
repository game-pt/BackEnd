package com.a405.gamept.game.util;

import java.util.HashMap;
import java.util.Map;

public interface GameData {
    // 몬스터 레벨 확률
    Map<Integer, Integer> monsterRate = new HashMap<>() {{
        put(2, 1);  // 레벨 2 차이 확률 1
        put(1, 2);  // 레벨 1 차이 확률 2
        put(0, 5);  // 레벨 0 차이 확률 5
        put(-1, 2);  // 레벨 1 차이 확률 2
        put(-2, 1);  // 레벨 2 차이 확률 1
    }};
    int PLAYER_MAX_LEVEL = 10;
    int MONSTER_MAX_LEVEL = 10;
    int MAX_STAT = 20;
    int MAX_ITEM_SIZE = 4;
}
