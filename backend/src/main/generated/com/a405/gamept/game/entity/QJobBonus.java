package com.a405.gamept.game.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QJobBonus is a Querydsl query type for JobBonus
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QJobBonus extends EntityPathBase<JobBonus> {

    private static final long serialVersionUID = 1080791744L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QJobBonus jobBonus = new QJobBonus("jobBonus");

    public final com.a405.gamept.global.entity.QBaseEntity _super = new com.a405.gamept.global.entity.QBaseEntity(this);

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final QJob job;

    public final QStat stat;

    public final NumberPath<Integer> statBonus = createNumber("statBonus", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QJobBonus(String variable) {
        this(JobBonus.class, forVariable(variable), INITS);
    }

    public QJobBonus(Path<? extends JobBonus> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QJobBonus(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QJobBonus(PathMetadata metadata, PathInits inits) {
        this(JobBonus.class, metadata, inits);
    }

    public QJobBonus(Class<? extends JobBonus> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.job = inits.isInitialized("job") ? new QJob(forProperty("job"), inits.get("job")) : null;
        this.stat = inits.isInitialized("stat") ? new QStat(forProperty("stat")) : null;
    }

}

