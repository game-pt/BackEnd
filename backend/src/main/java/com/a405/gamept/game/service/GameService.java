package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.DiceGetCommandDto;
import com.a405.gamept.game.dto.command.MonsterGetCommandDto;
import com.a405.gamept.game.dto.response.DiceGetResponseDto;
import com.a405.gamept.game.dto.response.MonsterGetResponseDto;
import com.a405.gamept.game.util.exception.GameInvalidException;
import com.a405.gamept.game.util.exception.MonsterInvalidException;
import com.a405.gamept.game.dto.command.RaceGetCommandDto;
import com.a405.gamept.game.dto.response.RaceGetResponseDto;
import com.a405.gamept.global.error.exception.BadRequestException;
import com.a405.gamept.global.error.exception.InternalServerException;
import java.util.List;
public interface GameService {
    /**
     * (진행중)
     * 스토리 별 종족 리스트를 조회한다
     * @param   : void
     * @return RaceGetResponseDto
     * @author  : 유영
     */
    List<RaceGetResponseDto> getRaceList(RaceGetCommandDto raceGetCommandDto) throws BadRequestException, InternalServerException;

    /**
     * 주사위(1~6) 2개를 돌린다
     * @param diceGetCommandDto : 게임 정보
     *                          gameCode : 현재 진행중인 게임 코드
     * @return : 랜덤으로 나온 주사위 값
     * @author : 지환
     */
    DiceGetResponseDto rollOfDice(DiceGetCommandDto diceGetCommandDto);

    /**
     * 게임 코드를 기반으로 Player가 플레이하는 게임이 맞는지 체크
     */
    boolean gameCheck(String gameCode, String playerCode);

    // void putRace(RacePutCommandDto racePutCommandDto) throws BadRequestException, InternalServerException;
}
