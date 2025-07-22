package com.example.campy.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTalentTagPost is a Querydsl query type for TalentTagPost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTalentTagPost extends EntityPathBase<TalentTagPost> {

    private static final long serialVersionUID = -1964664182L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTalentTagPost talentTagPost = new QTalentTagPost("talentTagPost");

    public final QTalent talent;

    public final QTalentTag talentTag;

    public final NumberPath<Integer> talentTagPostId = createNumber("talentTagPostId", Integer.class);

    public QTalentTagPost(String variable) {
        this(TalentTagPost.class, forVariable(variable), INITS);
    }

    public QTalentTagPost(Path<? extends TalentTagPost> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTalentTagPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTalentTagPost(PathMetadata metadata, PathInits inits) {
        this(TalentTagPost.class, metadata, inits);
    }

    public QTalentTagPost(Class<? extends TalentTagPost> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.talent = inits.isInitialized("talent") ? new QTalent(forProperty("talent"), inits.get("talent")) : null;
        this.talentTag = inits.isInitialized("talentTag") ? new QTalentTag(forProperty("talentTag")) : null;
    }

}

