package com.nhnacademy.bookstore.global.elastic.book.repository.impl;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.SearchTemplateQuery;
import org.springframework.data.elasticsearch.core.query.SearchTemplateQueryBuilder;
import org.springframework.stereotype.Repository;

import com.nhnacademy.bookstore.global.elastic.book.repository.ElasticSearchCustomBookRepository;
import com.nhnacademy.bookstore.global.elastic.document.book.BookDocument;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ElasticSearchCustomBookRepositoryImpl implements ElasticSearchCustomBookRepository {

	private final ElasticsearchOperations elasticsearchOperations;

	// @Override
	// public List<BookDocument> searchBooks(String keyword) {
	// 	BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
	// 		.should(QueryBuilders.matchQuery("title", keyword).boost(2))
	// 		.should(QueryBuilders.matchQuery("author", keyword).boost(1))
	// 		.should(QueryBuilders.matchQuery("publisher", keyword).boost(1))
	// 		.should(QueryBuilders.matchQuery("categoryList", keyword).boost(199));
	//
	// 	NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
	// 		.withQuery(boolQuery)
	// 		.build();
	//
	// 	SearchHits<BookDocument> searchHits = elasticsearchOperations.search(searchQuery, BookDocument.class);
	// 	return searchHits.getSearchHits().stream()
	// 		.map(hit -> hit.getContent())
	// 		.collect(Collectors.toList());
	// }
	// Query query = new CriteriaQuery(criteria).setPageable(pageable);

	@Override
	public SearchHits<BookDocument> searchProductsByProductName(String keyword, Pageable pageable) {
		// JSON 쿼리 작성
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
			.should(QueryBuilders.matchQuery("title", keyword).boost(2))
			.should(QueryBuilders.matchQuery("author", keyword).boost(3))
			.should(QueryBuilders.matchQuery("publisher", keyword).boost(1))
			.should(QueryBuilders.matchQuery("categoryList", keyword).boost(199));

		// // NativeSearchQuery 생성
		// Query searchQuery = new NativeSearchQuery(boolQuery);
		// searchQuery.setPageable(pageable);

		QueryBuilder query = QueryBuilders.boolQuery();
		SearchTemplateQueryBuilder searchTemplateQueryBuilder = new SearchTemplateQueryBuilder();
		Query query1 = new SearchTemplateQuery(searchTemplateQueryBuilder);
		// 검색 수행
		SearchHits<BookDocument> searchHits = elasticsearchOperations.search(query1, BookDocument.class);
		return searchHits;
	}

}
