package com.nhnacademy.bookstore.global.elastic.book.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstore.global.elastic.document.book.BookDocument;

public interface ElasticSearchBookRepository {

	Page<BookDocument> findByCustomQuery(String query, Pageable pageable);
}
