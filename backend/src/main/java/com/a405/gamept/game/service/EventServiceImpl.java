package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.request.FindEventByStoryCodeRequestDto;
import com.a405.gamept.game.entity.Event;
import com.a405.gamept.game.repository.EventRepository;
import com.a405.gamept.util.ChatGptClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final ChatGptClientUtil chatGptClientUtil;

    private final String EVENT_SEPARATOR_PRE = "[";
    private final String EVENT_SEPARATOR_LST = "]";

    /**
     * findAllEventByStoryCode <br/><br/>
     *
     * storyCode에 관련된 모든 Event를 조회.
     * @param storyDto
     * @return List&lt;Event&gt;
     */
    @Override
    public List<Event> findAllEventByStoryCode(FindEventByStoryCodeRequestDto storyDto) {
        String storyCode = storyDto.getStoryCode();

        return eventRepository.findByStoryCode(storyCode);
    }

    // 프롬프트 받고 이벤트 체크하는 거 만들어야 함.

    /**
     * pickAtRandomEventByStoryCode <br/><br/>
     *
     * storyCode에 관련된 Event 중 하나를 랜덤하게 선택.
     * @param storyDto
     * @return Event
     */
    public Event pickAtRandomEventByStoryCode(FindEventByStoryCodeRequestDto storyDto) {
        List<Event> eventList = findAllEventByStoryCode(storyDto);

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
        Event event = eventList.get(i);
        
        String answer = chatGptClientUtil.enterPrompt(event.getPrompt());
        if (answer != null) {

        }

        return eventList.get(i);
    }

    /**
     * findEventInText <br/><br/>
     *
     * 텍스트에서 이벤트가 위치한 인덱스를 반환.
     * @param text
     * @param eventName
     * @return <br/>
     * -1: 이벤트가 텍스트 내에 존재하지 않음.
     * >= 0: 이벤트가 텍스트 내에 존재하고, 해당 인덱스에 위치.
     */
    private int findEventInText(String text, String eventName) {
        return text.lastIndexOf(EVENT_SEPARATOR_PRE + text + EVENT_SEPARATOR_LST);
    }

    /**
     * findLastEventInText <br/><br/>
     *
     * 가장 마지막으로 등장한 이벤트를 반환. <br/>
     * @param eventNameList
     * @param text
     * @return <br/>
     * -1: 아무 이벤트도 찾지 못했음을 의미. <br/>
     * >= 0: 발견한 이벤트 중, 가장 마지막에 등장한 이벤트 리스트에서의 인덱스.
     */
    private int findLastEventInText(List<String> eventNameList, String text) {
        int lastEvent = -1;
        int lastEventIndex = -1;
        for (int i = 0; i < eventNameList.size(); i++) {
            int index = findEventInText(text, eventNameList.get(i));
            if (lastEventIndex < index) {
                lastEventIndex = index;
                lastEvent = i;
            }
        }

        return lastEvent;
    }

}
