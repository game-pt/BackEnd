package com.a405.gamept.game.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAct is a Querydsl query type for Act
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAct extends EntityPathBase<Act> {

    private static final long serialVersionUID = 441845108L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAct act = new QAct("act");

    public final com.a405.gamept.global.entity.QBaseEntity _super = new com.a405.gamept.global.entity.QBaseEntity(this);

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final QEvent event;

    public final NumberPath<Integer> extremeStd = createNumber("extremeStd", Integer.class);

    public final StringPath name = createString("name");

    public final QStat stat;

    public final EnumPath<Subtask> subtask = createEnum("subtask", Subtask.class);

    public final NumberPath<Integer> successStd = createNumber("successStd", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QAct(String variable) {
        this(Act.class, forVariable(variable), INITS);
    }

    public QAct(Path<? extends Act> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAct(PathMetadata metadata, PathInits inits) {
        this(Act.class, metadata, inits);
    }

    public QAct(Class<? extends Act> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.event = inits.isInitialized("event") ? new QEvent(forProperty("event"), inits.get("event")) : null;
        this.stat = inits.isInitialized("stat") ? new QStat(forProperty("stat")) : null;
    }

}

