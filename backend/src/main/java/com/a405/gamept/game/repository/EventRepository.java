package com.a405.gamept.game.repository;

import com.a405.gamept.game.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {

    List<Event> findByStory_StoryCode(String code);

}
