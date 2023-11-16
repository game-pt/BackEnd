package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.ActCommandDto;
import com.a405.gamept.game.dto.command.EventCommandDto;
import com.a405.gamept.game.dto.command.PromptResultGetCommandDto;
import com.a405.gamept.game.dto.response.PromptResultGetResponseDto;
import com.a405.gamept.game.entity.Act;
import com.a405.gamept.game.entity.Event;
import com.a405.gamept.game.repository.EventRepository;
import com.a405.gamept.game.util.enums.GameErrorMessage;
import com.a405.gamept.game.util.exception.GameException;
import com.a405.gamept.play.entity.Game;
import com.a405.gamept.play.repository.GameRedisRepository;
import com.a405.gamept.util.ChatGptClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final GameRedisRepository gameRedisRepository;

    private final ChatGptClientUtil chatGptClientUtil;

    private final String EVENT_SEPARATOR_PRE = "[";
    private final String EVENT_SEPARATOR_LST = "]";
    private final int EVENT_MAX_COUNT = 10;
    private final int MAX_TURN = 50;

    /**
     * findAllEventByStoryCode <br/><br/>
     *
     * storyCode에 관련된 모든 Event를 조회.
     * @param storyCode
     * @return List&lt;Event&gt;
     */
    @Override
    public List<Event> findAllEventByStoryCode(String storyCode) {;
        return eventRepository.findByStoryCode(storyCode)
                .orElseThrow(() -> new GameException(GameErrorMessage.EVENT_NOT_FOUND));
    }

    /**
     * pickAtRandomEventByStoryCode <br/><br/>
     * <p>
     * storyCode에 관련된 Event 중 하나를 랜덤하게 선택.
     *
     * @param game
     * @param prompt
     * @return Event
     */
    @Override
    @Transactional
    public String pickAtRandomEvent(Game game, String prompt) {
        // 게임 코드에 따른 게임 객체
//        Game game = gameRedisRepository.findById(promptResultGetCommandDto.gameCode())
//                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));

        // 이벤트가 발생하지 않음
        if (!checkRandomEvent(game.getEventRate())) {
            // 이벤트 랜덤 발생 확률 증가
            gameRedisRepository.save(game.toBuilder()
                    .eventRate(game.getEventRate() + 0.05)
                    .build());
            return prompt;
        }

        // 이벤트가 발생한 경우,
        // 스토리 코드에 따른 이벤트 리스트
        List<Event> eventList = eventRepository.findByStoryCode(game.getStoryCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.EVENT_NOT_FOUND));
        // 스토리 코드에 관련된 이벤트가 없는 경우
        if (eventList == null || eventList.size() == 0) {
            return prompt;
        }

        // 이벤트 중 랜덤하게 특정 이벤트 발생
        Event occuredEvent = occurRandomEvent(eventList);
        return prompt + occuredEvent.getPrompt();
    }

    /**
     * occurRandomEvent <br/><br/>
     *
     * 랜덤한 이벤트를 발생시키는 알고리즘.
     * @param eventList
     * @return index (이벤트 리스트 중 발생시킬 이벤트의 인덱스)
     */
    private Event occurRandomEvent(List<Event> eventList) {
        // 총 가중치 계산
        double randomStandard = Math.random();
        int allSum = 0;
        for (Event event : eventList) {
            allSum += event.getWeight();
        }

        // 랜덤 이벤트 추출
        double accSum = 0;
        int i = 0;
        for (; i < eventList.size(); i++) {
            Event event = eventList.get(i);
            accSum += ((double) event.getWeight()) / allSum;
            if (accSum >= randomStandard) break;
        }

        // eventList를 모두 통과하였음에도 불구하고, randomStandard에 도달하지 못한 경우,
        // accSum >= randomStandard가 0.999998 >= 0.999999... 이 되는 경우가 있을 수 있기 때문에
        if (i >= eventList.size()) i--;
        return eventList.get(i);
    }

    private boolean checkRandomEvent(double eventRate) {
        double occur = Math.random();

        if (occur <= eventRate) return true;
        return false;
    }

    /**
     * checkEventInPrompt <br/><br/>
     *
     * ChatGPT로부터 받은 프롬프트 안에 [이벤트]가 존재하는 경우, 발생된 이벤트를 반환.
     * @param game
     * @param prompt
     * @return
     */
    @Override
    @Transactional
    public EventCommandDto checkEventInPrompt(Game game, String prompt) {
        // 강제 마왕 발생 이벤트
        if (game.getTurn() >= MAX_TURN) {
            Event occuredEvent = eventRepository.findById("EV-007")  // 마왕 발생
                    .orElseThrow(() -> new GameException(GameErrorMessage.EVENT_NOT_FOUND));
            ArrayList<ActCommandDto> acts = new ArrayList<ActCommandDto>();
            for (Act act : occuredEvent.getActList()) {
                acts.add(ActCommandDto.from(act));
            }
            return EventCommandDto.from(occuredEvent, acts);
            
        // 더 이상 이벤트가 발생하지 않도록 예방
        }  else if (game.getEventCnt() > EVENT_MAX_COUNT) {
            return null;
        }

        // 스토리 코드에 따른 이벤트 리스트
        List<Event> eventList = findAllEventByStoryCode(game.getStoryCode());

        // 텍스트에서 가장 마지막에 등장한 이벤트의 인덱스
        int eventIndex = findLastEventInText(eventList, prompt);

        // 이벤트를 찾지 못한 경우,
        if (eventIndex == -1) {
            return null;
        }

        // 최종적으로 Response에 Event를 추가
        gameRedisRepository.save(game.toBuilder()
                .eventCnt(game.getEventCnt() + 1)
                .eventRate(0)
                .build());
        Event occuredEvent = eventList.get(eventIndex);
        ArrayList<ActCommandDto> acts = new ArrayList<ActCommandDto>();
        for (Act act : occuredEvent.getActList()) {
            acts.add(ActCommandDto.from(act));
        }
        return EventCommandDto.from(occuredEvent, acts);
    }

    /**
     * findEventInText <br/><br/>
     *
     * 텍스트에서 이벤트가 위치한 인덱스를 반환.
     * @param text
     * @param eventName
     * @return <br/>
     * -1: 이벤트가 텍스트 내에 존재하지 않음. <br/>
     * >= 0: 이벤트가 텍스트 내에 존재하고, 해당 인덱스에 위치.
     */
    private int findEventInText(String text, String eventName) {
        int left = text.lastIndexOf(EVENT_SEPARATOR_PRE + eventName);
        int right = text.lastIndexOf(eventName + EVENT_SEPARATOR_LST);
//        text.lastIndexOf(EVENT_SEPARATOR_PRE + eventName) > text.lastIndexOf(eventName + EVENT_SEPARATOR_LST) ?
//                        text.lastIndexOf(EVENT_SEPARATOR_PRE + eventName) :
//                        text.lastIndexOf(eventName + EVENT_SEPARATOR_LST);

        return left > right ? left : right;
    }

    /**
     * findLastEventInText <br/><br/>
     *
     * 가장 마지막으로 등장한 이벤트를 반환. <br/>
     * @param eventList
     * @param text
     * @return <br/>
     * -1: 아무 이벤트도 찾지 못했음을 의미. <br/>
     * >= 0: 발견한 이벤트 중, 가장 마지막에 등장한 이벤트 리스트에서의 인덱스.
     */
    private int findLastEventInText(List<Event> eventList, String text) {
        if (text == null) return -1;

        int lastEvent = -1;
        int lastEventIndex = -1;
        for (int i = 0; i < eventList.size(); i++) {
            int index = findEventInText(text, eventList.get(i).getName());
            if (lastEventIndex < index) {
                lastEventIndex = index;
                lastEvent = i;
            }
        }

        return lastEvent;
    }

}
