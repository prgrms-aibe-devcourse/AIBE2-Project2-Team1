package com.example.campy.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTalent is a Querydsl query type for Talent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTalent extends EntityPathBase<Talent> {

    private static final long serialVersionUID = 1948878512L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTalent talent = new QTalent("talent");

    public final StringPath availableDays = createString("availableDays");

    public final StringPath category = createString("category");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final BooleanPath deleted = createBoolean("deleted");

    public final StringPath description = createString("description");

    public final StringPath imagePath = createString("imagePath");

    public final StringPath offlineLocation = createString("offlineLocation");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final StringPath status = createString("status");

    public final SetPath<Tag, QTag> tags = this.<Tag, QTag>createSet("tags", Tag.class, QTag.class, PathInits.DIRECT2);

    public final NumberPath<Integer> talentId = createNumber("talentId", Integer.class);

    public final StringPath title = createString("title");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final QUser user;

    public QTalent(String variable) {
        this(Talent.class, forVariable(variable), INITS);
    }

    public QTalent(Path<? extends Talent> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTalent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTalent(PathMetadata metadata, PathInits inits) {
        this(Talent.class, metadata, inits);
    }

    public QTalent(Class<? extends Talent> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

