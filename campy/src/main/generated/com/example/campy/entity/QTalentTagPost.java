package com.example.campy.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTalentTagPost is a Querydsl query type for TalentTagPost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTalentTagPost extends EntityPathBase<TalentTagPost> {

    private static final long serialVersionUID = -1964664182L;

    public static final QTalentTagPost talentTagPost = new QTalentTagPost("talentTagPost");

    public final NumberPath<Integer> talentId = createNumber("talentId", Integer.class);

    public final NumberPath<Integer> talentTagId = createNumber("talentTagId", Integer.class);

    public final NumberPath<Integer> talentTagPostId = createNumber("talentTagPostId", Integer.class);

    public QTalentTagPost(String variable) {
        super(TalentTagPost.class, forVariable(variable));
    }

    public QTalentTagPost(Path<? extends TalentTagPost> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTalentTagPost(PathMetadata metadata) {
        super(TalentTagPost.class, metadata);
    }

}

