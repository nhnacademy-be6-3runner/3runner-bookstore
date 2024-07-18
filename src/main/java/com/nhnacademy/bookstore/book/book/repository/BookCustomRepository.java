package com.nhnacademy.bookstore.book.book.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstore.book.book.dto.response.BookListResponse;
import com.nhnacademy.bookstore.book.book.dto.response.BookManagementResponse;
import com.nhnacademy.bookstore.book.book.dto.response.ReadBookResponse;

/**
 * 도서 커스텀 레포지토리입니다.
 *
 * @author 김은비
 */
public interface BookCustomRepository {

	/**
	 * 도서 리스트를 불러오는 메서드입니다.
	 *
	 * @param pageable 페이지
	 * @return BookListResponse
	 */
	Page<BookListResponse> readBookList(Pageable pageable);

	/**
	 * 도서의 상세 정보를 불러오는 메서드 입니다.
	 *
	 * @author 한민기
	 *
	 * @param bookId  북 아이디
	 * @return 도서의 상세정보
	 */
	ReadBookResponse readDetailBook(Long bookId);

	Page<BookManagementResponse> readAdminBookList(Pageable pageable);

	Page<BookListResponse> readCategoryAllBookList(Pageable pageable, Long categoryId);
}
