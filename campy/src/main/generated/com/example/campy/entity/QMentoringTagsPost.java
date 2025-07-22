package com.example.campy.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMentoringTagsPost is a Querydsl query type for MentoringTagsPost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMentoringTagsPost extends EntityPathBase<MentoringTagsPost> {

    private static final long serialVersionUID = 2007087030L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMentoringTagsPost mentoringTagsPost = new QMentoringTagsPost("mentoringTagsPost");

    public final QMentoringOffer mentoringOffer;

    public final QMentoringTag mentoringTag;

    public final NumberPath<Integer> mentoringTagsPostId = createNumber("mentoringTagsPostId", Integer.class);

    public QMentoringTagsPost(String variable) {
        this(MentoringTagsPost.class, forVariable(variable), INITS);
    }

    public QMentoringTagsPost(Path<? extends MentoringTagsPost> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMentoringTagsPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMentoringTagsPost(PathMetadata metadata, PathInits inits) {
        this(MentoringTagsPost.class, metadata, inits);
    }

    public QMentoringTagsPost(Class<? extends MentoringTagsPost> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.mentoringOffer = inits.isInitialized("mentoringOffer") ? new QMentoringOffer(forProperty("mentoringOffer"), inits.get("mentoringOffer")) : null;
        this.mentoringTag = inits.isInitialized("mentoringTag") ? new QMentoringTag(forProperty("mentoringTag")) : null;
    }

}

