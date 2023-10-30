package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.RaceGetCommandDto;
import com.a405.gamept.game.dto.response.RaceGetResponseDto;
import com.a405.gamept.game.util.exception.GameException;

import java.util.List;

public interface GameStartService {
    /**
     * (완료)
     * 스토리 별 종족 리스트를 조회한다
     * @param   : void
     * @return RaceGetResponseDto
     * @author  : 유영
     */
    List<RaceGetResponseDto> getRaceList(RaceGetCommandDto raceGetCommandDto) throws GameException;
}
