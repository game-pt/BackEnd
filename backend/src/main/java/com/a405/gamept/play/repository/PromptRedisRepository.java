package com.a405.gamept.play.repository;

import com.a405.gamept.play.entity.Prompt;
import org.springframework.data.repository.CrudRepository;

public interface PromptRedisRepository extends CrudRepository<Prompt, String> {

}
