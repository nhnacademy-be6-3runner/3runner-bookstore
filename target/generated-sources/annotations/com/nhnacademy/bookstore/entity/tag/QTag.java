package com.nhnacademy.bookstore.entity.tag;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTag is a Querydsl query type for Tag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTag extends EntityPathBase<Tag> {

    private static final long serialVersionUID = 1755802106L;

    public static final QTag tag = new QTag("tag");

    public final ListPath<com.nhnacademy.bookstore.entity.bookTag.BookTag, com.nhnacademy.bookstore.entity.bookTag.QBookTag> bookTagList = this.<com.nhnacademy.bookstore.entity.bookTag.BookTag, com.nhnacademy.bookstore.entity.bookTag.QBookTag>createList("bookTagList", com.nhnacademy.bookstore.entity.bookTag.BookTag.class, com.nhnacademy.bookstore.entity.bookTag.QBookTag.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QTag(String variable) {
        super(Tag.class, forVariable(variable));
    }

    public QTag(Path<? extends Tag> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTag(PathMetadata metadata) {
        super(Tag.class, metadata);
    }

}

