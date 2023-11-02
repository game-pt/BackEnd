package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.command.ActCommandDto;
import com.a405.gamept.game.dto.command.EventCommandDto;
import com.a405.gamept.game.dto.command.FindEventByStoryCodeCommandDto;
import com.a405.gamept.game.dto.command.GetPromptResultCommandDto;
import com.a405.gamept.game.dto.response.GetPromptResultResponseDto;
import com.a405.gamept.game.entity.Act;
import com.a405.gamept.game.entity.Event;
import com.a405.gamept.game.entity.Story;
import com.a405.gamept.game.repository.EventRepository;
import com.a405.gamept.game.repository.StoryRepository;
import com.a405.gamept.game.util.enums.GameErrorMessage;
import com.a405.gamept.game.util.exception.GameException;
import com.a405.gamept.play.entity.Game;
import com.a405.gamept.play.repository.GameRedisRepository;
import com.a405.gamept.util.ChatGptClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final StoryRepository storyRepository;
    private final GameRedisRepository gameRedisRepository;

    private final ChatGptClientUtil chatGptClientUtil;

    private final String EVENT_SEPARATOR_PRE = "[";
    private final String EVENT_SEPARATOR_LST = "]";

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

    // 프롬프트 받고 이벤트 체크하는 거 만들어야 함.

    /**
     * pickAtRandomEventByStoryCode <br/><br/>
     * <p>
     * storyCode에 관련된 Event 중 하나를 랜덤하게 선택.
     *
     * @param getPromptResultCommandDto
     * @return Event
     */
    public GetPromptResultCommandDto pickAtRandomEvent(GetPromptResultCommandDto getPromptResultCommandDto) {
        // 게임 코드에 따른 게임 객체
        Game game = gameRedisRepository.findById(getPromptResultCommandDto.code())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));

        // 이벤트가 발생하지 않음
        if (!checkRandomEvent(game.getEventRate())) {
            return getPromptResultCommandDto;
        }

        // 이벤트가 발생한 경우,
        // 스토리 코드에 따른 이벤트 리스트
        List<Event> eventList = eventRepository.findByStoryCode(game.getStoryCode())
                .orElseThrow(() -> new GameException(GameErrorMessage.EVENT_NOT_FOUND));

        // 이벤트 중 랜덤하게 특정 이벤트 발생
        Event occuredEvent = occurRandomEvent(eventList);
        String newPrompt = getPromptResultCommandDto.prompt() + occuredEvent.getPrompt();
        return GetPromptResultCommandDto.from(getPromptResultCommandDto, newPrompt);
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
     * @param getPromptResultCommandDto
     * @return
     */
    @Override
    public GetPromptResultResponseDto checkEventInPrompt(GetPromptResultCommandDto getPromptResultCommandDto) {
        // 게임 코드에 따른 게임 객체
        Game game = gameRedisRepository.findById(getPromptResultCommandDto.code())
                .orElseThrow(() -> new GameException(GameErrorMessage.GAME_NOT_FOUND));

        // 스토리 코드에 따른 이벤트 리스트
        List<Event> eventList = findAllEventByStoryCode(game.getStoryCode());

//        Story story = storyRepository.findById(game.getStoryCode())
//                .orElseThrow(() -> new GameException(GameErrorMessage.STORY_NOT_FOUND));

        // 해당 스토리가 갖는 Event 종류들
//        List<Event> eventList = story.getEventList();

        // 텍스트에서 가장 마지막에 등장한 이벤트의 인덱스
        int eventIndex = findLastEventInText(eventList, getPromptResultCommandDto.prompt());

        // 이벤트를 찾지 못한 경우,
        if (eventIndex == -1) {
            return GetPromptResultResponseDto.from(getPromptResultCommandDto, null);
        }

        // 최종적으로 Response에 Event를 추가
        Event occuredEvent = eventList.get(eventIndex);
        ArrayList<ActCommandDto> acts = new ArrayList<ActCommandDto>();
        for (Act act : occuredEvent.getActList()) {
            acts.add(ActCommandDto.from(act));
        }
        EventCommandDto eventCommandDto = EventCommandDto.from(occuredEvent, acts);

        return GetPromptResultResponseDto.from(getPromptResultCommandDto, eventCommandDto);
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
        return text.lastIndexOf(EVENT_SEPARATOR_PRE + eventName + EVENT_SEPARATOR_LST);
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
