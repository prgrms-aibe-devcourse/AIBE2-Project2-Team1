package com.example.campy.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMentoringMatchMember is a Querydsl query type for MentoringMatchMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMentoringMatchMember extends EntityPathBase<MentoringMatchMember> {

    private static final long serialVersionUID = 1580295234L;

    public static final QMentoringMatchMember mentoringMatchMember = new QMentoringMatchMember("mentoringMatchMember");

    public final DateTimePath<java.time.LocalDateTime> joinedAt = createDateTime("joinedAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> matchId = createNumber("matchId", Integer.class);

    public final NumberPath<Integer> memberId = createNumber("memberId", Integer.class);

    public final NumberPath<Integer> requestId = createNumber("requestId", Integer.class);

    public final StringPath role = createString("role");

    public final StringPath status = createString("status");

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QMentoringMatchMember(String variable) {
        super(MentoringMatchMember.class, forVariable(variable));
    }

    public QMentoringMatchMember(Path<? extends MentoringMatchMember> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMentoringMatchMember(PathMetadata metadata) {
        super(MentoringMatchMember.class, metadata);
    }

}

