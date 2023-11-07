package com.a405.gamept.game.repository;

import com.a405.gamept.game.entity.Stat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatRepository extends JpaRepository<Stat, String> {
}
