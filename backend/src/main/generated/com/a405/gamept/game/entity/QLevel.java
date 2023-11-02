package com.a405.gamept.game.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLevel is a Querydsl query type for Level
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLevel extends EntityPathBase<Level> {

    private static final long serialVersionUID = -578390042L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLevel level1 = new QLevel("level1");

    public final com.a405.gamept.global.entity.QBaseEntity _super = new com.a405.gamept.global.entity.QBaseEntity(this);

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Integer> exp = createNumber("exp", Integer.class);

    public final NumberPath<Integer> level = createNumber("level", Integer.class);

    public final QStory story;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QLevel(String variable) {
        this(Level.class, forVariable(variable), INITS);
    }

    public QLevel(Path<? extends Level> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLevel(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLevel(PathMetadata metadata, PathInits inits) {
        this(Level.class, metadata, inits);
    }

    public QLevel(Class<? extends Level> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.story = inits.isInitialized("story") ? new QStory(forProperty("story")) : null;
    }

}

