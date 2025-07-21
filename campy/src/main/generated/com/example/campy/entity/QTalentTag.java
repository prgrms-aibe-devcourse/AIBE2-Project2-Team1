package com.example.campy.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTalentTag is a Querydsl query type for TalentTag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTalentTag extends EntityPathBase<TalentTag> {

    private static final long serialVersionUID = -328072502L;

    public static final QTalentTag talentTag = new QTalentTag("talentTag");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> talentTagId = createNumber("talentTagId", Integer.class);

    public QTalentTag(String variable) {
        super(TalentTag.class, forVariable(variable));
    }

    public QTalentTag(Path<? extends TalentTag> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTalentTag(PathMetadata metadata) {
        super(TalentTag.class, metadata);
    }

}

