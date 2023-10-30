package com.a405.gamept.game.repository;

import com.a405.gamept.game.entity.Race;
import com.a405.gamept.game.entity.Story;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RaceRepository extends JpaRepository<Race, String> {
    Optional<List<Race>> findAllByStory(Story story) throws DataAccessException;
}
