package com.example.campy.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTalent is a Querydsl query type for Talent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTalent extends EntityPathBase<Talent> {

    private static final long serialVersionUID = 1948878512L;

    public static final QTalent talent = new QTalent("talent");

    public final StringPath availableDays = createString("availableDays");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final StringPath imagePath = createString("imagePath");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath offlineLocation = createString("offlineLocation");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final StringPath status = createString("status");

    public final NumberPath<Integer> talentId = createNumber("talentId", Integer.class);

    public final StringPath title = createString("title");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QTalent(String variable) {
        super(Talent.class, forVariable(variable));
    }

    public QTalent(Path<? extends Talent> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTalent(PathMetadata metadata) {
        super(Talent.class, metadata);
    }

}

