package com.example.campy.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAiCnt is a Querydsl query type for AiCnt
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAiCnt extends EntityPathBase<AiCnt> {

    private static final long serialVersionUID = -1062859299L;

    public static final QAiCnt aiCnt = new QAiCnt("aiCnt");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> searchedAt = createDateTime("searchedAt", java.time.LocalDateTime.class);

    public final StringPath tagList = createString("tagList");

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QAiCnt(String variable) {
        super(AiCnt.class, forVariable(variable));
    }

    public QAiCnt(Path<? extends AiCnt> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAiCnt(PathMetadata metadata) {
        super(AiCnt.class, metadata);
    }

}

