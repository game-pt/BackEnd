package com.a405.gamept.game.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStat is a Querydsl query type for Stat
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStat extends EntityPathBase<Stat> {

    private static final long serialVersionUID = 812848562L;

    public static final QStat stat = new QStat("stat");

    public final com.a405.gamept.global.entity.QBaseEntity _super = new com.a405.gamept.global.entity.QBaseEntity(this);

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QStat(String variable) {
        super(Stat.class, forVariable(variable));
    }

    public QStat(Path<? extends Stat> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStat(PathMetadata metadata) {
        super(Stat.class, metadata);
    }

}

