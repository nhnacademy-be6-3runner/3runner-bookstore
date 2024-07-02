package com.nhnacademy.bookstore.book.book.repository.impl;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.nhnacademy.bookstore.book.book.dto.response.BookListResponse;
import com.nhnacademy.bookstore.book.book.dto.response.ReadBookResponse;
import com.nhnacademy.bookstore.book.book.repository.BookRepository;
import com.nhnacademy.bookstore.book.bookCartegory.repository.impl.BookCategoryCustomRepositoryImpl;
import com.nhnacademy.bookstore.book.bookImage.repository.BookImageRepository;
import com.nhnacademy.bookstore.entity.book.Book;
import com.nhnacademy.bookstore.entity.bookImage.BookImage;
import com.nhnacademy.bookstore.entity.bookImage.enums.BookImageType;
import com.nhnacademy.bookstore.entity.totalImage.TotalImage;
import com.nhnacademy.bookstore.purchase.purchaseBook.exception.NotExistsBook;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DataJpaTest
@Import(BookCategoryCustomRepositoryImpl.class)
class BookCustomRepositoryImplTest {

	@Autowired
	private BookCustomRepositoryImpl bookCustomRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BookImageRepository bookImageRepository;

	Book book;
	BookImage bookImage;
	TotalImage totalImage;

	@BeforeEach
	public void setup() {
		book = new Book(
			"Test Title",
			"Test Description",
			ZonedDateTime.now(),
			1000,
			10,
			900,
			0,
			true,
			"Test Author",
			"123456789",
			"Test Publisher",
			null,
			null,
			null
		);
		totalImage = new TotalImage("test.png");

		bookImage = new BookImage(BookImageType.MAIN, book, totalImage);

		bookRepository.save(book);
		bookImageRepository.save(bookImage);

	}

	@Test
	void readBookList() {
		Page<BookListResponse> content = bookCustomRepository.readBookList(PageRequest.of(0, 10));

		assertThat(content).isNotNull();

	}

	@Test
	void readDetailBook() {
		long searchId = book.getId();
		ReadBookResponse book = bookCustomRepository.readDetailBook(searchId);

		assertThat(book).isNotNull();
	}

	@Test
	void readDetailBookException() {
		long searchId = book.getId();
		assertThrows(NotExistsBook.class, () -> bookCustomRepository.readDetailBook(searchId + 123));
	}
}