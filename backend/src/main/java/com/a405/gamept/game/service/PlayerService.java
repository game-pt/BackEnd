package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.JobGetCommandDto;
import com.a405.gamept.game.dto.command.PlayerSetCommandDto;
import com.a405.gamept.game.dto.command.RaceGetCommandDto;
import com.a405.gamept.game.dto.response.JobGetResponseDto;
import com.a405.gamept.game.dto.response.PlayerSetResponseDto;
import com.a405.gamept.game.dto.response.RaceGetResponseDto;
import com.a405.gamept.game.util.exception.GameException;

import java.util.List;

public interface PlayerService {
    /**
     * (완료)
     * 스토리 별 종족 리스트를 조회한다
     * @param raceGetCommandDto: 플레이어가 속한 게임방 정보
     * @return RaceGetResponseDto
     * @author  : 유영
     */
    List<RaceGetResponseDto> getRaceList(RaceGetCommandDto raceGetCommandDto) throws GameException;

    /**
     * (완료)
     * 스토리 별 직업 리스트를 조회한다
     * @param jobGetCommandDto : 플레이어가 속한 게임방 정보
     * @return JobGetResponseDto
     * @author  : 유영
     */
    List<JobGetResponseDto> getJobList(JobGetCommandDto jobGetCommandDto) throws GameException;

    /**
     * (진행 중)
     * 해당 게임 방의 플레이어를 삽입한다.
     * @param playerSetCommandDto : 플레이어를 삽입할 게임방 정보
     * @return JobGetResponseDto
     * @author  : 유영
     */
    PlayerSetResponseDto setPlayer(PlayerSetCommandDto playerSetCommandDto) throws GameException;
}
