package com.a405.gamept.game.service;

import com.a405.gamept.game.dto.FindEventByStoryCodeReq;
import com.a405.gamept.game.entity.Event;
import com.a405.gamept.game.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    /**
     * findAllEventByStoryCode <br/><br/>
     *
     * storyCode에 관련된 모든 Event를 조회.
     * @param storyDto
     * @return List&lt;Event&gt;
     */
    @Override
    public List<Event> findAllEventByStoryCode(FindEventByStoryCodeReq storyDto) {
        String storyCode = storyDto.getStoryCode();

        return eventRepository.findByStory_StoryCode(storyCode);
    }

    /**
     * pickAtRandomEventByStoryCode <br/><br/>
     *
     * storyCode에 관련된 Event 중 하나를 랜덤하게 선택.
     * @param storyDto
     * @return Event
     */
    public Event pickAtRandomEventByStoryCode(FindEventByStoryCodeReq storyDto) {
        List<Event> eventList = findAllEventByStoryCode(storyDto);

        double randomStandard = Math.random();
        int allSum = 0;
        for (Event event : eventList) {
            allSum += event.getWeight();
        }

        double accSum = 0;
        int i = 0;
        for (; i < eventList.size(); i++) {
            Event event = eventList.get(i);
            accSum += ((double) event.getWeight()) / allSum;
            if (accSum >= randomStandard) break;
        }

        return eventList.get(i);
    }

}
