package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.ActGetCommandDto;
import com.a405.gamept.game.dto.command.ActResultGetCommandDto;
import com.a405.gamept.game.dto.command.DiceGetCommandDto;
import com.a405.gamept.game.dto.command.GameSetCommandDto;
import com.a405.gamept.game.dto.command.StoryGetCommandDto;
import com.a405.gamept.game.dto.command.SubtaskCommandDto;
import com.a405.gamept.game.dto.response.ActGetResponseDto;
import com.a405.gamept.game.dto.response.DiceGetResponseDto;
import com.a405.gamept.game.dto.response.GameSetResponseDto;
import com.a405.gamept.game.dto.response.PromptResultGetResponseDto;
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

    /**
     * 선택한 행동을 수행한 후 GPT에게 prompt를 보낸 후 받아와서 반환한다.
     * @param actResultGetCommandDto : 선택한 행동과 수행한 플레이어의 정보
     *                               actCode : 어떤 행동을 선택했느지
     *                               gameCode : 어떤 게임에서 일어나는지
     *                               playerCode : 누가 수행하는지
     * @return : 게임 코드와 prompt
     */
    PromptResultGetResponseDto playAct(ActResultGetCommandDto actResultGetCommandDto);
}
