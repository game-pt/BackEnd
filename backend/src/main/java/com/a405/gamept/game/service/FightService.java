package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.MonsterGetCommandDto;
import com.a405.gamept.game.dto.response.MonsterGetResponseDto;
import com.a405.gamept.game.util.exception.GameInvalidException;
import com.a405.gamept.game.util.exception.MonsterInvalidException;

public interface FightService {

    /**
     * 플레이어의 레벨에 맞는 몬스터를 조회한다.
     * @param monsterGetCommandDto : 게임 정보
     *                             storyCode    : 현재 진행중인 스토리
     *                             level        : 현재 플레이어의 레벨
     * @return : 랜덤으로 뽑아져나온 몬스터의 값
     * @author : 유영
     */
    MonsterGetResponseDto getMonster(MonsterGetCommandDto monsterGetCommandDto) throws GameInvalidException, MonsterInvalidException;
}
