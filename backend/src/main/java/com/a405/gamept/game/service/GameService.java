package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.*;
import com.a405.gamept.game.dto.response.*;

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
     * (완료)
     *  채팅을 전송한다.
     * @param chatCommandDto : 플레이어가 입력한 채팅 정보
     * @return : 채팅 메시지 및 게임 코드
     * @author : 유영
     */
    ChatResponseDto chat(ChatCommandDto chatCommandDto);

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
    List<ActGetResponseDto> getOptions(ActGetCommandDto actGetCommandDto);

    /**
     * 행동의 하위 항목 목록을 반환
     * @param subtaskCommandDto : 해당 행동의 정보
     *                          playerCode : 어떤 플레이어의 정보를 가져올지
     *                          subtask : 어떤 행동의 하위 목록을 가져와야하는지
     * @return : 하위 항목의 코드, 이름 리스트
     * @author : 지환
     */
    List<SubtaskResponseDto> getSubtask(SubtaskCommandDto subtaskCommandDto);
}
