package com.a405.gamept.game.repository;

import com.a405.gamept.game.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {

    Optional<List<Event>> findByStoryCode(String code);
    Optional<List<Event>> findAllByStoryCode(String code);

}
