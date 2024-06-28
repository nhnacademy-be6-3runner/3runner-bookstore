package com.nhnacademy.bookstore.book.book.service;

import com.nhnacademy.bookstore.book.book.dto.request.CreateBookRequest;
import com.nhnacademy.bookstore.book.book.dto.response.ReadBookResponse;
//

/**
 * 책 테이블 CRUD 서비스.
 *
 * @author 김병우
 */
public interface BookService {
	/**
	 * 책 등록 기능.
	 *
	 * @param createBookRequest createBookRequest form param
	 */
	public Long createBook(CreateBookRequest createBookRequest);

	/**
	 * 책 조회 기능.
	 *
	 * @param bookId book entity id param
	 * @return Book
	 */
	public ReadBookResponse readBookById(Long bookId);
}
