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
     * [pk] gameCode: 아이템 추가 스탯 코드
     */
    @Id
    private String code;

    /**
     * [fk] item_code: 아이템의 gameCode
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_code")
    private Item item;

    /**
     * [fk] stat_code: 스탯의 gameCode
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stat_code")
    private Stat stat;

    /**
     * statBonus: 추가 스탯
     */
    @Column(columnDefinition = "SMALLINT")
    private int statBonus;

    @Builder
    public ItemStat(String code, Item item, Stat stat, int statBonus) {
        this.code = code;
        this.item = item;
        this.stat = stat;
        this.statBonus = statBonus;
    }

}