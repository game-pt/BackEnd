package com.a405.gamept.game.repository;

import com.a405.gamept.game.entity.ActStat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActStatRepository extends JpaRepository<ActStat, String> {

    Optional<List<ActStat>> findAllByActCode(String code);
}
