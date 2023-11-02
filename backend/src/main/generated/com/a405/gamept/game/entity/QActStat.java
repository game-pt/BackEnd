package com.a405.gamept.game.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QActStat is a Querydsl query type for ActStat
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QActStat extends EntityPathBase<ActStat> {

    private static final long serialVersionUID = 1280681448L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QActStat actStat = new QActStat("actStat");

    public final com.a405.gamept.global.entity.QBaseEntity _super = new com.a405.gamept.global.entity.QBaseEntity(this);

    public final QAct act;

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final QStat stat;

    public final NumberPath<Integer> statBonus = createNumber("statBonus", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QActStat(String variable) {
        this(ActStat.class, forVariable(variable), INITS);
    }

    public QActStat(Path<? extends ActStat> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QActStat(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QActStat(PathMetadata metadata, PathInits inits) {
        this(ActStat.class, metadata, inits);
    }

    public QActStat(Class<? extends ActStat> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.act = inits.isInitialized("act") ? new QAct(forProperty("act"), inits.get("act")) : null;
        this.stat = inits.isInitialized("stat") ? new QStat(forProperty("stat")) : null;
    }

}

