package com.example.campy.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMentoringMatch is a Querydsl query type for MentoringMatch
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMentoringMatch extends EntityPathBase<MentoringMatch> {

    private static final long serialVersionUID = 676115336L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMentoringMatch mentoringMatch = new QMentoringMatch("mentoringMatch");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> matchId = createNumber("matchId", Integer.class);

    public final QMentoringOffer mentoringOffer;

    public final StringPath status = createString("status");

    public final StringPath type = createString("type");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QMentoringMatch(String variable) {
        this(MentoringMatch.class, forVariable(variable), INITS);
    }

    public QMentoringMatch(Path<? extends MentoringMatch> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMentoringMatch(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMentoringMatch(PathMetadata metadata, PathInits inits) {
        this(MentoringMatch.class, metadata, inits);
    }

    public QMentoringMatch(Class<? extends MentoringMatch> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.mentoringOffer = inits.isInitialized("mentoringOffer") ? new QMentoringOffer(forProperty("mentoringOffer"), inits.get("mentoringOffer")) : null;
    }

}

