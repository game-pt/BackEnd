package com.a405.gamept.game.repository;

import com.a405.gamept.game.entity.Act;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActRepository extends JpaRepository<Act, String> {
    Optional<List<Act>> findAllByEventCode(String eventCode);
}
