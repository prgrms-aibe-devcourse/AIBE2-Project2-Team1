package com.example.campy.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMentoringTagsPost is a Querydsl query type for MentoringTagsPost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMentoringTagsPost extends EntityPathBase<MentoringTagsPost> {

    private static final long serialVersionUID = 2007087030L;

    public static final QMentoringTagsPost mentoringTagsPost = new QMentoringTagsPost("mentoringTagsPost");

    public final NumberPath<Integer> mentoringRequestId = createNumber("mentoringRequestId", Integer.class);

    public final NumberPath<Integer> mentoringTagId = createNumber("mentoringTagId", Integer.class);

    public final NumberPath<Integer> mentoringTagsPostId = createNumber("mentoringTagsPostId", Integer.class);

    public QMentoringTagsPost(String variable) {
        super(MentoringTagsPost.class, forVariable(variable));
    }

    public QMentoringTagsPost(Path<? extends MentoringTagsPost> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMentoringTagsPost(PathMetadata metadata) {
        super(MentoringTagsPost.class, metadata);
    }

}

