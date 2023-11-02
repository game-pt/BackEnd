package com.a405.gamept.game.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRaceStat is a Querydsl query type for RaceStat
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRaceStat extends EntityPathBase<RaceStat> {

    private static final long serialVersionUID = -1636621021L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRaceStat raceStat = new QRaceStat("raceStat");

    public final com.a405.gamept.global.entity.QBaseEntity _super = new com.a405.gamept.global.entity.QBaseEntity(this);

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final QRace race;

    public final QStat stat;

    public final NumberPath<Integer> statValue = createNumber("statValue", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QRaceStat(String variable) {
        this(RaceStat.class, forVariable(variable), INITS);
    }

    public QRaceStat(Path<? extends RaceStat> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRaceStat(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRaceStat(PathMetadata metadata, PathInits inits) {
        this(RaceStat.class, metadata, inits);
    }

    public QRaceStat(Class<? extends RaceStat> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.race = inits.isInitialized("race") ? new QRace(forProperty("race"), inits.get("race")) : null;
        this.stat = inits.isInitialized("stat") ? new QStat(forProperty("stat")) : null;
    }

}

