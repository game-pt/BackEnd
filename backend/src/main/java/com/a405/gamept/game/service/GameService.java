package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.RaceGetCommandDto;
import com.a405.gamept.game.dto.response.RaceGetResponseDto;
import com.a405.gamept.game.util.exception.GameException;

import java.util.List;

public interface GameService {
    /**
     * (진행중)
     * 스토리 별 종족 리스트를 조회한다
     * @param   : void
     * @return RaceGetResponseDto
     * @author  : 유영
     */
    List<RaceGetResponseDto> getRaceList(RaceGetCommandDto raceGetCommandDto) throws GameException;

    /**
     * (진행예정)
     * 유저의 선택 종족을 유저 정보에 저장한다.
     * @param racePutCommandDto :   roomCode    - 방 코드
     *                              playerCode  - 플레이어 코드
     *                              raceCode    - 종족 코드
     * @return : void
     * @author : 유영
     */
    // void putRace(RacePutCommandDto racePutCommandDto) throws BadRequestException, InternalServerException;
}
