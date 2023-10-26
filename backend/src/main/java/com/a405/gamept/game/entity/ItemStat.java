package com.a405.gamept.game.entity;

import com.a405.gamept.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ItemStat Entity
 *
 * 아이템 별로 추가로 증가하는 스탯 정의.
 */
@Entity
@Table(name = "item_stat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemStat extends BaseEntity {

    /**
     * [pk] code: 아이템 추가 스탯 코드
     */
    @Id
    private String code;

    /**
     * [fk] item_code: 아이템의 code
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_code")
    private Item item;

    /**
     * [fk] stat_code: 스탯의 code
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stat_code")
    private Stat stat;

    /**
     * stat_bonus: 추가 스탯
     */
    private short stat_bonus;

    @Builder
    public ItemStat(String code, Item item, Stat stat, short stat_bonus) {
        this.code = code;
        this.item = item;
        this.stat = stat;
        this.stat_bonus = stat_bonus;
    }

}
