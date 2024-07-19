package com.nhnacademy.bookstore.global.elastic.book.repository.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.nhnacademy.bookstore.global.elastic.book.repository.ElasticSearchBookRepository;
import com.nhnacademy.bookstore.global.elastic.document.book.BookDocument;

@Repository
public class ElasticSearchBookRepositoryImpl implements ElasticSearchBookRepository {

	// @Query("{"
	// 	+ "    \"function_score\": {"
	// 	+ "      \"query\": {"
	// 	+ "        \"multi_match\": {"
	// 	+ "          \"query\": \"?0\", "
	// 	+ "          \"minimum_should_match\": \"70%\","
	// 	+ "          \"fields\": [\"title^70\", \"titleNgram^70\", \"author^50\", \"publisher^50\", \"categoryList^50\", \"tagList^60\"]"
	// 	+ "        }"
	// 	+ "      }"
	// 	+ "    }"
	// 	+ "  }")

	@Override
	public Page<BookDocument> findByCustomQuery(String query, Pageable pageable) {
		return null;
	}
}
