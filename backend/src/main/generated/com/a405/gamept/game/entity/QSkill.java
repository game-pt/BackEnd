package com.a405.gamept.game.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSkill is a Querydsl query type for Skill
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSkill extends EntityPathBase<Skill> {

    private static final long serialVersionUID = -571758925L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSkill skill = new QSkill("skill");

    public final com.a405.gamept.global.entity.QBaseEntity _super = new com.a405.gamept.global.entity.QBaseEntity(this);

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath desc = createString("desc");

    public final NumberPath<Integer> extremeStd = createNumber("extremeStd", Integer.class);

    public final QJob job;

    public final StringPath name = createString("name");

    public final QStat stat;

    public final NumberPath<Integer> successStd = createNumber("successStd", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QSkill(String variable) {
        this(Skill.class, forVariable(variable), INITS);
    }

    public QSkill(Path<? extends Skill> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSkill(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSkill(PathMetadata metadata, PathInits inits) {
        this(Skill.class, metadata, inits);
    }

    public QSkill(Class<? extends Skill> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.job = inits.isInitialized("job") ? new QJob(forProperty("job"), inits.get("job")) : null;
        this.stat = inits.isInitialized("stat") ? new QStat(forProperty("stat")) : null;
    }

}

