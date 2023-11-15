package com.a405.gamept.game.dto.command;

import jakarta.validation.constraints.NotBlank;

/**
 * 사용자 또는 몬스터가 죽었는지 파악하기 위해 내부 로직에서 사용하는 record
 * @param fightingEnemyHp : 현재 전투 상황
 * @param fightingEnemyExp : 전투 종료 여부
 * @author : 지환
 */

public record FightingEnemyStateCommandDto(
        int fightingEnemyHp,
        int fightingEnemyExp,
        String prompt
) {
        public FightingEnemyStateCommandDto of(int fightingEnemyHp, int fightingEnemyExp, String prompt){
                return new FightingEnemyStateCommandDto(fightingEnemyHp, fightingEnemyExp, prompt);
        }
}
