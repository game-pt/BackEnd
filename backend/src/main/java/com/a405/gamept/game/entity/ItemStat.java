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
     * effectValue: 아이템의 영향력
     * 데미지량, 회복량, 스탯업량 등
     */
    @Column(columnDefinition = "SMALLINT")
    private int effectValue;

    @Builder
    public ItemStat(String code, Item item, Stat stat, int effectValue) {
        this.code = code;
        this.item = item;
        this.stat = stat;
        this.effectValue = effectValue;
    }

}