package com.example.campy.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMentoringMatch is a Querydsl query type for MentoringMatch
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMentoringMatch extends EntityPathBase<MentoringMatch> {

    private static final long serialVersionUID = 676115336L;

    public static final QMentoringMatch mentoringMatch = new QMentoringMatch("mentoringMatch");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> matchId = createNumber("matchId", Integer.class);

    public final NumberPath<Integer> requestId = createNumber("requestId", Integer.class);

    public final StringPath status = createString("status");

    public final StringPath type = createString("type");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QMentoringMatch(String variable) {
        super(MentoringMatch.class, forVariable(variable));
    }

    public QMentoringMatch(Path<? extends MentoringMatch> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMentoringMatch(PathMetadata metadata) {
        super(MentoringMatch.class, metadata);
    }

}

