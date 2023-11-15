package com.a405.gamept.game.repository;

import com.a405.gamept.game.entity.Skill;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, String> {
    List<Skill> findAllByJobCode(String code);
}
