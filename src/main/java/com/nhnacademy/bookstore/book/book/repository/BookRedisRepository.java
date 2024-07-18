package com.nhnacademy.bookstore.book.book.repository;

import com.nhnacademy.bookstore.entity.book.Book;

public interface BookRedisRepository {
	void createBook(Book book);

	void updateBook(Book book);

	void deleteBook(long bookId);
}
