package com.a405.gamept.play.repository;

import com.a405.gamept.play.entity.FightingEnermy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FightingEnermyRedisRepository extends CrudRepository<FightingEnermy, String> {
}
