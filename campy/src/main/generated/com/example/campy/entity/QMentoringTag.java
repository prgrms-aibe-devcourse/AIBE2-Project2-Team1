package com.example.campy.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMentoringTag is a Querydsl query type for MentoringTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMentoringTag extends EntityPathBase<MentoringTag> {

    private static final long serialVersionUID = 1082273309L;

    public static final QMentoringTag mentoringTag = new QMentoringTag("mentoringTag");

    public final NumberPath<Integer> mentoringTagId = createNumber("mentoringTagId", Integer.class);

    public final StringPath name = createString("name");

    public QMentoringTag(String variable) {
        super(MentoringTag.class, forVariable(variable));
    }

    public QMentoringTag(Path<? extends MentoringTag> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMentoringTag(PathMetadata metadata) {
        super(MentoringTag.class, metadata);
    }

}

