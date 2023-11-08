package com.a405.gamept.play.repository;

import com.a405.gamept.play.entity.Gmonster;
import com.a405.gamept.play.entity.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GmonsterRedisRepository extends CrudRepository<Gmonster, String> {
}
