package com.a405.gamept.game.repository;

import com.a405.gamept.game.entity.Job;
import com.a405.gamept.game.entity.Story;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, String> {
    Optional<List<Job>> findAllByStory(Story story) throws DataAccessException;
}
