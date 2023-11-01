package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.DiceGetCommandDto;
import com.a405.gamept.game.dto.command.GameSetCommandDto;
import com.a405.gamept.game.dto.command.StoryGetCommandDto;
import com.a405.gamept.game.dto.response.DiceGetResponseDto;
import com.a405.gamept.game.dto.response.GameSetResponseDto;
import com.a405.gamept.game.dto.response.StoryGetResponseDto;

import java.util.List;
public interface GameService {
    /**
     * (완료)
     * 스토리 리스트를 조회한다.
     * @param   : void
     * @return  : 스토리 리스트
     * @author  : 유영
     */
    List<StoryGetResponseDto> getStoryList();

    /**
     * (완료)
     * 스토리 코드로 스토리를 조회한다.
     * @param storyGetCommandDto : 스토리 코드
     * @return : 스토리
     * @author : 유영
     */
    StoryGetResponseDto getStory(StoryGetCommandDto storyGetCommandDto);

    /**
     * (완료)
     * 스토리 코드로 게임을 생성한다.
     * @param gameSetCommandDto : 스토리 코드
     * @return : 게임 정보
     * @author : 유영
     */
    GameSetResponseDto setGame(GameSetCommandDto gameSetCommandDto);

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

}
