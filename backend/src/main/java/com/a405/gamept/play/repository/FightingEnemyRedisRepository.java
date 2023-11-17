package com.a405.gamept.play.repository;

import com.a405.gamept.play.entity.FightingEnemy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FightingEnemyRedisRepository extends CrudRepository<FightingEnemy, String> {
    // Optional<FightingEnermy> findByGameCode(String gameCode);
}
