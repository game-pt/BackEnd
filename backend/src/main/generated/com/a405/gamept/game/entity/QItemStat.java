package com.a405.gamept.game.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItemStat is a Querydsl query type for ItemStat
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItemStat extends EntityPathBase<ItemStat> {

    private static final long serialVersionUID = -394697627L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemStat itemStat = new QItemStat("itemStat");

    public final com.a405.gamept.global.entity.QBaseEntity _super = new com.a405.gamept.global.entity.QBaseEntity(this);

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final QItem item;

    public final QStat stat;

    public final NumberPath<Integer> statBonus = createNumber("statBonus", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QItemStat(String variable) {
        this(ItemStat.class, forVariable(variable), INITS);
    }

    public QItemStat(Path<? extends ItemStat> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItemStat(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItemStat(PathMetadata metadata, PathInits inits) {
        this(ItemStat.class, metadata, inits);
    }

    public QItemStat(Class<? extends ItemStat> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new QItem(forProperty("item"), inits.get("item")) : null;
        this.stat = inits.isInitialized("stat") ? new QStat(forProperty("stat")) : null;
    }

}

