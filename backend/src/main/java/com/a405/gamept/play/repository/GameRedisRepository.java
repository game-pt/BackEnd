package com.a405.gamept.play.repository;

import com.a405.gamept.play.entity.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRedisRepository extends CrudRepository<Game, String> {

}
