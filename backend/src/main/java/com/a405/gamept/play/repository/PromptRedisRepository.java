package com.a405.gamept.play.repository;

import com.a405.gamept.play.entity.Prompt;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromptRedisRepository extends CrudRepository<Prompt, String> {
    Optional<Prompt> findByCode(String promptCode);
}