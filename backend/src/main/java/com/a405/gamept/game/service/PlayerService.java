package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.*;
import com.a405.gamept.game.dto.response.*;
import com.a405.gamept.game.util.exception.GameException;

import java.util.List;

public interface PlayerService {
    /**
     * (완료)
     * 스토리 별 종족 리스트를 조회한다
     * @param raceGetCommandDto: 플레이어가 속한 게임방 정보
     * @return  : 스토리 별 종족 리스트
     * @author  : 유영
     */
    List<RaceGetResponseDto> getRaceList(RaceGetCommandDto raceGetCommandDto) throws GameException;

    /**
     * (완료)
     * 스토리 별 직업 리스트를 조회한다
     * @param jobGetCommandDto : 플레이어가 속한 게임방 정보
     * @return  : 스토리 별 직업 리스트
     * @author  : 유영
     */
    List<JobGetResponseDto> getJobList(JobGetCommandDto jobGetCommandDto) throws GameException;

    /**
     * (완료)
     * 해당 게임 방의 플레이어를 삽입한다.
     * @param playerSetCommandDto : 플레이어를 삽입할 게임방 정보
     * @return  : 플레이어의 이름
     * @author  : 유영
     */
    PlayerSetResponseDto setPlayer(PlayerSetCommandDto playerSetCommandDto) throws GameException;

    /**
     * (완료)
     * 해당 게임 방의 플레이어를 조회한다.
     * @param playerGetCommandDto : 조회할 플레이어 정보
     * @return  : 플레이어 정보
     * @author  : 유영
     */
    PlayerGetResponseDto getPlayer(PlayerGetCommandDto playerGetCommandDto) throws GameException;

    /**
     * 해당 플레이어의 스탯을 조회한다.
     * @param playerStatGetCommandDto
     * @return  : 플레이어 스탯
     * @author  : 이우석
     */
    PlayerStatGetResponseDto getPlayerStat(PlayerStatGetCommandDto playerStatGetCommandDto) throws GameException;

    /**
     * 해당 플레이어의 스탯을 추가한다.
     * @param playerStatUpdateCommandDto
     * @return  : 플레이어 스탯
     * @author  : 이우석
     */
    PlayerStatGetResponseDto addPlayerStat(PlayerStatUpdateCommandDto playerStatUpdateCommandDto) throws GameException;

    /**
     * 해당 플레이어의 HP를 수정한다.
     * @param playerHpUpdateCommandDto
     * @return  : 플레이어 스테이터스 (웹소켓)
     * @author  : 이우석
     */
    void updatePlayerHp(PlayerHpUpdateCommandDto playerHpUpdateCommandDto) throws GameException;

    /**
     * 해당 플레이어의 경험치를 수정한다.
     * @param playerExpUpdateCommandDto
     * @return  : 플레이어 스테이터스 (웹소켓)
     * @author  : 이우석
     */
    void updatePlayerExp(PlayerExpUpdateCommandDto playerExpUpdateCommandDto) throws GameException;

    /**
     * 해당 플레이어의 레벨을 업 시킨다.
     * @param playerLevelUpdateCommand
     * @return  : 플레이어 스테이터스 (웹소켓)
     * @author  : 이우석
     */
    void updatePlayerLevel(PlayerLevelUpdateCommand playerLevelUpdateCommand) throws GameException;
}
