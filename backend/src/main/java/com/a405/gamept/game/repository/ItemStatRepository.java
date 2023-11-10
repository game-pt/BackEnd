package com.a405.gamept.game.repository;

import com.a405.gamept.game.entity.Item;
import com.a405.gamept.game.entity.ItemStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemStatRepository extends JpaRepository<ItemStat, String> {
    Optional<List<ItemStat>> findAllByItemCode(String itemCode);

    Optional<ItemStat> findByItemCode(String itemCode);
}
