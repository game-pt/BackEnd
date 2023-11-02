package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.ActGetCommandDto;
import com.a405.gamept.game.dto.command.DiceGetCommandDto;
import com.a405.gamept.game.dto.command.StoryGetCommandDto;
import com.a405.gamept.game.dto.response.ActGetResponseDto;
import com.a405.gamept.game.dto.response.DiceGetResponseDto;

import com.a405.gamept.game.dto.response.StoryGetResponseDto;
import com.a405.gamept.game.dto.response.SubtaskResponseDto;
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
     * 주사위(1~6) 2개를 돌린다
     * @param diceGetCommandDto : 게임 정보
     *                          gameCode : 현재 진행중인 게임 코드
     * @return : 랜덤으로 나온 주사위 값
     * @author : 지환
     */
    DiceGetResponseDto rollOfDice(DiceGetCommandDto diceGetCommandDto);

    /**
     * 게임 코드를 기반으로 Player가 플레이하는 게임이 맞는지 체크
     * @param gameCode : 참가 중인 게임 번호
     * @param playerCode : 참가자 본인 넘버
     * @return : 본인이 해당 게임에 참여 중인지에 대한 여부 : True or False
     * @author : 지환
     */
    boolean gameCheck(String gameCode, String playerCode);

    /**
     * 현재 발생한 이벤트의 선택지 목록 반환
     * @param actGetCommandDto : 이벤트 정보
     *                         eventCode : 선택지를 가르키는 이벤트 코드
     * @return : 본인이 해당 게임에 참여 중인지에 대한 여부 : True or False
     * @author : 지환
     */
    List<ActGetResponseDto> optionsGet(ActGetCommandDto actGetCommandDto);
}
