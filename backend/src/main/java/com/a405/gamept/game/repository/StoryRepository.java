package com.a405.gamept.game.repository;

import com.a405.gamept.game.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepository extends JpaRepository<Story, String> {
}
