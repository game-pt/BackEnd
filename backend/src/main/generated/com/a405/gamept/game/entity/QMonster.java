package com.a405.gamept.game.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMonster is a Querydsl query type for Monster
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMonster extends EntityPathBase<Monster> {

    private static final long serialVersionUID = -615214148L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMonster monster = new QMonster("monster");

    public final com.a405.gamept.global.entity.QBaseEntity _super = new com.a405.gamept.global.entity.QBaseEntity(this);

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Integer> exp = createNumber("exp", Integer.class);

    public final NumberPath<Integer> level = createNumber("level", Integer.class);

    public final ListPath<MonsterStat, QMonsterStat> monsterStatList = this.<MonsterStat, QMonsterStat>createList("monsterStatList", MonsterStat.class, QMonsterStat.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final QStory story;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QMonster(String variable) {
        this(Monster.class, forVariable(variable), INITS);
    }

    public QMonster(Path<? extends Monster> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMonster(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMonster(PathMetadata metadata, PathInits inits) {
        this(Monster.class, metadata, inits);
    }

    public QMonster(Class<? extends Monster> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.story = inits.isInitialized("story") ? new QStory(forProperty("story")) : null;
    }

}

