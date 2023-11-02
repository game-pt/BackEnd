package com.a405.gamept.game.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMonsterStat is a Querydsl query type for MonsterStat
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMonsterStat extends EntityPathBase<MonsterStat> {

    private static final long serialVersionUID = 861130800L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMonsterStat monsterStat = new QMonsterStat("monsterStat");

    public final com.a405.gamept.global.entity.QBaseEntity _super = new com.a405.gamept.global.entity.QBaseEntity(this);

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final QMonster monster;

    public final QStat stat;

    public final NumberPath<Integer> statValue = createNumber("statValue", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QMonsterStat(String variable) {
        this(MonsterStat.class, forVariable(variable), INITS);
    }

    public QMonsterStat(Path<? extends MonsterStat> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMonsterStat(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMonsterStat(PathMetadata metadata, PathInits inits) {
        this(MonsterStat.class, metadata, inits);
    }

    public QMonsterStat(Class<? extends MonsterStat> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.monster = inits.isInitialized("monster") ? new QMonster(forProperty("monster"), inits.get("monster")) : null;
        this.stat = inits.isInitialized("stat") ? new QStat(forProperty("stat")) : null;
    }

}

