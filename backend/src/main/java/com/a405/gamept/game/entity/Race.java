package com.a405.gamept.game.entity;

import com.a405.gamept.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

/**
 * Race
 *
 * 플레이어가 선택할 수 있는 캐릭터의 종족을 정의.
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Race extends BaseEntity {
    /**
     * code : pk
     * */
    @Id
    private String code;

    /**
     * name : 종족명
     * */
    private String name;
    
    @OneToMany(mappedBy = "race")
    @Comment("종족 스탯 리스트")
    private List<RaceStat> raceStatList;

    /**
     * [fk] stat : 이 종족이 어떤 스토리에서 사용되는지
     * */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_code")
    private Story story;
}