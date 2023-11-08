package com.a405.gamept.game.repository;

import com.a405.gamept.game.entity.Item;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {

    Optional<List<Item>> findAllByStoryCode(String storyCode);

    Optional<Item> findByCodeAndStoryCode(String itemCode, String StoryCode);
}
