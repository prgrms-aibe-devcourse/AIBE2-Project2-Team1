package com.example.campy.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMentoringRequest is a Querydsl query type for MentoringRequest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMentoringRequest extends EntityPathBase<MentoringRequest> {

    private static final long serialVersionUID = 1461610386L;

    public static final QMentoringRequest mentoringRequest = new QMentoringRequest("mentoringRequest");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> duration = createNumber("duration", Integer.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath location = createString("location");

    public final NumberPath<Integer> maxParticipants = createNumber("maxParticipants", Integer.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Integer> requestId = createNumber("requestId", Integer.class);

    public final StringPath status = createString("status");

    public final StringPath title = createString("title");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QMentoringRequest(String variable) {
        super(MentoringRequest.class, forVariable(variable));
    }

    public QMentoringRequest(Path<? extends MentoringRequest> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMentoringRequest(PathMetadata metadata) {
        super(MentoringRequest.class, metadata);
    }

}

