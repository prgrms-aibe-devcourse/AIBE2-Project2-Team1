package com.example.campy.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAiCnt is a Querydsl query type for AiCnt
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAiCnt extends EntityPathBase<AiCnt> {

    private static final long serialVersionUID = -1062859299L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAiCnt aiCnt = new QAiCnt("aiCnt");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> searchedAt = createDateTime("searchedAt", java.time.LocalDateTime.class);

    public final StringPath tagList = createString("tagList");

    public final QUser user;

    public QAiCnt(String variable) {
        this(AiCnt.class, forVariable(variable), INITS);
    }

    public QAiCnt(Path<? extends AiCnt> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAiCnt(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAiCnt(PathMetadata metadata, PathInits inits) {
        this(AiCnt.class, metadata, inits);
    }

    public QAiCnt(Class<? extends AiCnt> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

