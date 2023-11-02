package com.a405.gamept.game.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRace is a Querydsl query type for Race
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRace extends EntityPathBase<Race> {

    private static final long serialVersionUID = 812800559L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRace race = new QRace("race");

    public final com.a405.gamept.global.entity.QBaseEntity _super = new com.a405.gamept.global.entity.QBaseEntity(this);

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath name = createString("name");

    public final ListPath<RaceStat, QRaceStat> raceStatList = this.<RaceStat, QRaceStat>createList("raceStatList", RaceStat.class, QRaceStat.class, PathInits.DIRECT2);

    public final QStory story;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QRace(String variable) {
        this(Race.class, forVariable(variable), INITS);
    }

    public QRace(Path<? extends Race> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRace(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRace(PathMetadata metadata, PathInits inits) {
        this(Race.class, metadata, inits);
    }

    public QRace(Class<? extends Race> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.story = inits.isInitialized("story") ? new QStory(forProperty("story")) : null;
    }

}

