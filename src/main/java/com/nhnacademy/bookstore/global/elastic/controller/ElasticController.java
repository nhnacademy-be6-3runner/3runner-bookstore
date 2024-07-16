package com.nhnacademy.bookstore.global.elastic.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstore.book.book.dto.response.ReadBookResponse;
import com.nhnacademy.bookstore.book.book.service.BookService;
import com.nhnacademy.bookstore.book.bookCartegory.service.BookCategoryService;
import com.nhnacademy.bookstore.book.bookTag.dto.request.ReadBookIdRequest;
import com.nhnacademy.bookstore.book.bookTag.dto.response.ReadTagByBookResponse;
import com.nhnacademy.bookstore.book.bookTag.service.BookTagService;
import com.nhnacademy.bookstore.global.elastic.book.repository.ElasticSearchBookRepository;
import com.nhnacademy.bookstore.global.elastic.document.book.BookDocument;
import com.nhnacademy.bookstore.util.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/bookstore/books/search")
@RequiredArgsConstructor
public class ElasticController {
	private final ElasticSearchBookRepository elasticSearchBookRepository;
	private final BookService bookService;
	private final BookCategoryService bookCategoryService;
	private final BookTagService bookTagService;

	/**
	 * 책 밀어 넣어주기
	 * 나중에 배치서버로 이동시키거나 삭제할 예정
	 * @return 성공 메시지
	 */
	@GetMapping("/push")
	String push() {
		for (long i = 1; i <= 713; i++) {
			try {
				ReadBookResponse book = bookService.readBookById(i);

				List<String> categoryList = bookCategoryService.readBookCategoryNames(
					i);
				List<ReadTagByBookResponse> readTagList =
					bookTagService.readTagByBookId(ReadBookIdRequest.builder().bookId(i).build());

				List<String> tagList = new ArrayList<>();
				for (ReadTagByBookResponse tag : readTagList) {
					tagList.add(tag.name());
				}

				BookDocument bookDocument = new BookDocument(
					book.id(),
					book.title(),
					book.author(),
					book.imagePath(),
					book.publisher(),
					tagList,
					categoryList,
					book.price(),
					book.sellingPrice()
				);

				elasticSearchBookRepository.save(bookDocument);
			} catch (Exception e) {
				log.info("{}, 번째", i);
			}
		}

		return "good";
	}

	/**
	 * 검색 결과 보내주기
	 * @param page 페이지
	 * @param size    페이지 사이즈
	 * @param keyword    검색 키워드
	 * @return 검색으로 찾은 책들
	 */
	@GetMapping
	public ApiResponse<Page<BookDocument>> searchKeyWord(@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "12") int size,
		@RequestParam("keyword") String keyword) {

		Page<BookDocument> pageBookDocument = elasticSearchBookRepository.findByCustomQuery(keyword,
			PageRequest.of(page, size));
		return ApiResponse.success(pageBookDocument);
	}

}