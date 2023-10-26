package com.a405.gamept.game.repository;

import com.a405.gamept.game.entity.Monster;
import com.a405.gamept.game.entity.Story;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonsterRepository extends JpaRepository<Monster, String> {

    List<Monster> findAllByStoryAndLevel(Story story, int level) throws DataAccessException;
}
