package com.nhnacademy.bookstore.book.book.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstore.book.book.dto.request.CreateBookRequest;
import com.nhnacademy.bookstore.book.book.dto.response.BookListResponse;
import com.nhnacademy.bookstore.book.book.dto.response.ReadBookResponse;
import com.nhnacademy.bookstore.book.book.exception.BookDoesNotExistException;
import com.nhnacademy.bookstore.book.book.repository.BookRepository;
import com.nhnacademy.bookstore.book.book.service.impl.BookServiceImpl;
import com.nhnacademy.bookstore.entity.book.Book;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
	@Mock
	private BookRepository bookRepository;
	@InjectMocks
	private BookServiceImpl bookService;

	@Test
	public void testCreateBook() {
		CreateBookRequest request = new CreateBookRequest(
			"Test Title",
			"Test Description",
			ZonedDateTime.now(),
			1000,
			10,
			900,
			true,
			"Test Author",
			"123456789",
			"Test Publisher",
			"asdf.jpg",
			List.of("a.jpg", "b.jpg"),
			List.of(1L, 2L, 3L),
			List.of(1L, 2L, 3L)
		);

		bookService.createBook(request);

		assertThat(request.imageList().size()).isEqualTo(2);
		assertThat(request.tagIds().size()).isEqualTo(3);
		assertThat(request.categoryIds().size()).isEqualTo(3);
		verify(bookRepository, times(1)).save(any(Book.class));
	}

	@Test
	public void testReadBookById_Success() {
		ReadBookResponse readBookResponse = ReadBookResponse.builder()
			.id(1L)
			.title("test Title")
			.description("Test description")
			.publishedDate(ZonedDateTime.now())
			.price(10000)
			.quantity(10)
			.sellingPrice(10000)
			.viewCount(777)
			.packing(true)
			.author("Test Author")
			.isbn("1234567890123")
			.publisher("Test Publisher")
			.imagePath("Test Image Path")

			.build();

		when(bookRepository.readDetailBook(anyLong())).thenReturn(readBookResponse);

		ReadBookResponse foundBook = bookService.readBookById(1L);

		assertEquals(readBookResponse.id(), foundBook.id());
	}

	@Test
	public void testReadBookById_NotFound() {
		when(bookRepository.readDetailBook(anyLong())).thenReturn(null);

		assertThrows(BookDoesNotExistException.class, () -> bookService.readBookById(1L));

	}

	@Test
	public void testReadAllBooks_Success() {
		Pageable pageable = PageRequest.of(0, 10);
		BookListResponse bookResponse =
			BookListResponse.builder()
				.id(1L)
				.title("test Title")
				.price(12344)
				.sellingPrice(1234444)
				.author("Test Author")
				.thumbnail("Test Thumbnail")
				.build();
		Page<BookListResponse> bookPage = new PageImpl<>(Collections.singletonList(bookResponse), pageable, 1);

		when(bookRepository.readBookList(any(Pageable.class))).thenReturn(bookPage);

		assertEquals(bookPage.getTotalElements(), bookService.readAllBooks(pageable).getTotalElements());
	}
}