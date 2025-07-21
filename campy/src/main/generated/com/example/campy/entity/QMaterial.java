package com.example.campy.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMaterial is a Querydsl query type for Material
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMaterial extends EntityPathBase<Material> {

    private static final long serialVersionUID = 1180883435L;

    public static final QMaterial material = new QMaterial("material");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath fileUrl = createString("fileUrl");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final NumberPath<Integer> materialId = createNumber("materialId", Integer.class);

    public final StringPath previewFileUrl = createString("previewFileUrl");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Integer> sellerId = createNumber("sellerId", Integer.class);

    public final StringPath thumbnailUrl = createString("thumbnailUrl");

    public final StringPath title = createString("title");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QMaterial(String variable) {
        super(Material.class, forVariable(variable));
    }

    public QMaterial(Path<? extends Material> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMaterial(PathMetadata metadata) {
        super(Material.class, metadata);
    }

}

