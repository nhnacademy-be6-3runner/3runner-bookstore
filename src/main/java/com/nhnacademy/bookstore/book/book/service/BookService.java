package com.nhnacademy.bookstore.book.book.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstore.book.book.dto.request.CreateBookRequest;
import com.nhnacademy.bookstore.book.book.dto.response.BookForCouponResponse;
import com.nhnacademy.bookstore.book.book.dto.response.BookListResponse;
import com.nhnacademy.bookstore.book.book.dto.response.BookManagementResponse;
import com.nhnacademy.bookstore.book.book.dto.response.ReadBookResponse;

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
	void createBook(CreateBookRequest createBookRequest);

	/**
	 * 책 조회 기능.
	 *
	 * @param bookId book entity id param
	 * @return Book
	 */
	public ReadBookResponse readBookById(Long bookId);

	void updateBook(Long bookId, CreateBookRequest createBookRequest);

	Page<BookListResponse> readAllBooks(Pageable pageable);

	Page<BookManagementResponse> readAllAdminBooks(Pageable pageable);

	void deleteBook(Long bookId);

	List<BookForCouponResponse> readBookByIds(List<Long> ids);

	Page<BookListResponse> readCategoryAllBooks(Pageable pageable, Long categoryId);

	void addView(Long bookId);
}
