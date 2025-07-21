package com.example.campy.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMentoringMatchDetail is a Querydsl query type for MentoringMatchDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMentoringMatchDetail extends EntityPathBase<MentoringMatchDetail> {

    private static final long serialVersionUID = 1322840569L;

    public static final QMentoringMatchDetail mentoringMatchDetail = new QMentoringMatchDetail("mentoringMatchDetail");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> detailId = createNumber("detailId", Integer.class);

    public final NumberPath<Integer> durationMinutes = createNumber("durationMinutes", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> endTime = createDateTime("endTime", java.time.LocalDateTime.class);

    public final NumberPath<Integer> matchId = createNumber("matchId", Integer.class);

    public final NumberPath<Integer> requestId = createNumber("requestId", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> startTime = createDateTime("startTime", java.time.LocalDateTime.class);

    public final StringPath subject = createString("subject");

    public QMentoringMatchDetail(String variable) {
        super(MentoringMatchDetail.class, forVariable(variable));
    }

    public QMentoringMatchDetail(Path<? extends MentoringMatchDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMentoringMatchDetail(PathMetadata metadata) {
        super(MentoringMatchDetail.class, metadata);
    }

}

