package com.nhnacademy.bookstore.book.bookcategory.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstore.book.book.dto.response.BookListResponse;
import com.nhnacademy.bookstore.book.category.dto.response.BookDetailCategoryResponse;
import org.springframework.transaction.annotation.Transactional;

public interface BookCategoryCustomRepository {
	Page<BookListResponse> categoryWithBookList(Long categoryId, Pageable pageable);

	@Transactional
	List<BookDetailCategoryResponse> bookWithCategoryList(Long bookId);

	Page<BookListResponse> categoriesWithBookList(List<Long> categoryList, Pageable pageable);
}
