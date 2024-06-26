package com.nhnacademy.bookstore.book.book.repository.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.nhnacademy.bookstore.book.book.dto.response.BookListResponse;
import com.nhnacademy.bookstore.book.book.dto.response.ReadBookResponse;
import com.nhnacademy.bookstore.book.book.repository.BookCustomRepository;
import com.nhnacademy.bookstore.entity.book.QBook;
import com.nhnacademy.bookstore.entity.bookImage.QBookImage;
import com.nhnacademy.bookstore.entity.bookImage.enums.BookImageType;
import com.nhnacademy.bookstore.purchase.purchaseBook.exception.NotExistsBook;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class BookCustomRepositoryImpl implements BookCustomRepository {
	private final JPAQueryFactory jpaQueryFactory;
	private final QBook qBook = QBook.book;
	private final QBookImage qBookImage = QBookImage.bookImage;

	public BookCustomRepositoryImpl(EntityManager entityManager) {
		this.jpaQueryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public Page<BookListResponse> readBookList(Pageable pageable) {
		List<BookListResponse> content = jpaQueryFactory.select(
				Projections.constructor(BookListResponse.class,
					qBook.id,
					qBook.title,
					qBook.price,
					qBook.sellingPrice,
					qBook.author,
					qBookImage.url))
			.from(qBook)
			.leftJoin(qBookImage)
			.on(qBookImage.book.id.eq(qBook.id).and(qBookImage.type.eq(BookImageType.MAIN)))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
		return null;
	}

	@Override
	public ReadBookResponse readDetailBook(Long bookId) {
		List<ReadBookResponse> content = jpaQueryFactory.select(Projections.constructor(ReadBookResponse.class,
				qBook.id,
				qBook.title,
				qBook.description,
				qBook.publishedDate,
				qBook.price,
				qBook.quantity,
				qBook.sellingPrice,
				qBook.viewCount,
				qBook.packing,
				qBook.author,
				qBook.isbn,
				qBook.publisher,
				qBookImage.url))
			.from(qBook)
			.leftJoin(qBookImage)
			.on(qBookImage.book.id.eq(qBook.id).and(qBookImage.type.eq(BookImageType.MAIN)))
			.where(qBook.id.eq(bookId))
			.limit(1)
			.fetch();

		if (content.isEmpty()) {
			throw new NotExistsBook();
		}
		return content.getFirst();
	}
}


