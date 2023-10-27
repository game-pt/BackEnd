package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.DiceGetCommandDto;
import com.a405.gamept.game.dto.command.MonsterGetCommandDto;
import com.a405.gamept.game.dto.response.DiceGetResponseDto;
import com.a405.gamept.game.dto.response.MonsterGetResponseDto;
import com.a405.gamept.game.util.exception.GameInvalidException;
import com.a405.gamept.game.util.exception.MonsterInvalidException;

public interface GameService {  // 추후 변경 가능

    /**
     * 플레이어의 레벨에 맞는 몬스터를 조회한다.
     * @param monsterGetCommandDto : 게임 정보
     *                             storyCode    : 현재 진행중인 스토리
     *                             level        : 현재 플레이어의 레벨
     * @return : 랜덤으로 뽑아져나온 몬스터의 값
     * @author : 유영
     */
    MonsterGetResponseDto getMonster(MonsterGetCommandDto monsterGetCommandDto) throws GameInvalidException, MonsterInvalidException;

    /**
     * 주사위(1~6) 2개를 돌린다
     * @param diceGetCommandDto : 게임 정보
     *                          gameCode : 현재 진행중인 게임 코드
     * @return : 랜덤으로 나온 주사위 값
     * @author : 지환
     */
    DiceGetResponseDto RollOfDice(DiceGetCommandDto diceGetCommandDto);
}
