package com.a405.gamept.play.repository;

import com.a405.gamept.play.entity.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRedisRepository extends CrudRepository<Player, String> {
}
