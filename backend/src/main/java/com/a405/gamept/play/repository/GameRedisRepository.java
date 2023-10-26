package com.a405.gamept.play.repository;

import com.a405.gamept.play.entity.Game;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRedisRepository extends CrudRepository<Game, String> {
    Optional<Game> findByCode(String gameCode);
}
