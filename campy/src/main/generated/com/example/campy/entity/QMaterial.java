package com.example.campy.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMaterial is a Querydsl query type for Material
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMaterial extends EntityPathBase<Material> {

    private static final long serialVersionUID = 1180883435L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMaterial material = new QMaterial("material");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath fileUrl = createString("fileUrl");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final NumberPath<Integer> materialId = createNumber("materialId", Integer.class);

    public final StringPath previewFileUrl = createString("previewFileUrl");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final QUser seller;

    public final StringPath thumbnailUrl = createString("thumbnailUrl");

    public final StringPath title = createString("title");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QMaterial(String variable) {
        this(Material.class, forVariable(variable), INITS);
    }

    public QMaterial(Path<? extends Material> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMaterial(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMaterial(PathMetadata metadata, PathInits inits) {
        this(Material.class, metadata, inits);
    }

    public QMaterial(Class<? extends Material> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.seller = inits.isInitialized("seller") ? new QUser(forProperty("seller")) : null;
    }

}

