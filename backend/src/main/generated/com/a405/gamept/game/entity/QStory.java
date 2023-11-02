package com.a405.gamept.game.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStory is a Querydsl query type for Story
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStory extends EntityPathBase<Story> {

    private static final long serialVersionUID = -571484841L;

    public static final QStory story = new QStory("story");

    public final com.a405.gamept.global.entity.QBaseEntity _super = new com.a405.gamept.global.entity.QBaseEntity(this);

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath desc = createString("desc");

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedDate = _super.updatedDate;

    public QStory(String variable) {
        super(Story.class, forVariable(variable));
    }

    public QStory(Path<? extends Story> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStory(PathMetadata metadata) {
        super(Story.class, metadata);
    }

}

