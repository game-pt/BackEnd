package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.FightResultGetCommandDto;
import com.a405.gamept.game.dto.command.MonsterGetCommandDto;
import com.a405.gamept.game.dto.command.MonsterSetCommandDto;
import com.a405.gamept.game.dto.response.FightResultGetResponseDto;
import com.a405.gamept.game.dto.response.MonsterGetResponseDto;
import com.a405.gamept.game.util.exception.GameException;

public interface FightService {

    /**
     * 플레이어의 레벨에 맞는 몬스터 레벨을 조회한다.
     * @param  : 게임 정보
     *                             storyCode    : 현재 진행중인 스토리
     *                             level        : 현재 플레이어의 레벨
     * @return : 랜덤으로 뽑아져나온 몬스터의 값
     * @author : 유영
     */
    // int getRandomMonsterLevel(MonsterGetCommandDto monsterGetCommandDto) throws GameException;
    void setMonster(MonsterSetCommandDto monsterSetCommandDto) throws GameException;
    MonsterGetResponseDto getMonster(MonsterGetCommandDto monsterGetCommandDto) throws GameException;

    FightResultGetResponseDto getFightResult(FightResultGetCommandDto fightResultGetCommandDto);
}
