package com.nhnacademy.bookstore.book.bookImage.service.Impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nhnacademy.bookstore.book.book.repository.BookRepository;
import com.nhnacademy.bookstore.book.bookImage.dto.request.CreateBookImageRequest;
import com.nhnacademy.bookstore.book.bookImage.repository.BookImageRepository;
import com.nhnacademy.bookstore.book.image.exception.NotFindImageException;
import com.nhnacademy.bookstore.entity.book.Book;
import com.nhnacademy.bookstore.entity.bookImage.BookImage;
import com.nhnacademy.bookstore.entity.bookImage.enums.BookImageType;

class BookImageServiceImplTest {

	@Mock
	private BookImageRepository bookImageRepository;

	@Mock
	private BookRepository bookRepository;

	@InjectMocks
	private BookImageServiceImpl bookImageServiceImpl;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void createBookImage_withList_shouldSaveAllBookImages() {
		// Given
		List<String> imageList = List.of("url1", "url2");
		Book book = new Book("Sample Book", "Sample Description", ZonedDateTime.now(), 100, 50, 80, 500, true,
			"12346789",
			"John Doe", "Sample Publisher", null, null, null);

		when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

		// When
		bookImageServiceImpl.createBookImage(imageList, 1L, BookImageType.MAIN);

		// Then
		verify(bookImageRepository, times(2)).save(any(BookImage.class));
	}

	@Test
	void createBookImage_withValidList_shouldSaveAllBookImages() {
		// Given
		CreateBookImageRequest request1 = new CreateBookImageRequest("url1", BookImageType.MAIN, 1L);
		CreateBookImageRequest request2 = new CreateBookImageRequest("url2", BookImageType.DESCRIPTION, 1L);
		List<CreateBookImageRequest> requestList = Arrays.asList(request1, request2);

		Book book = new Book("Sample Book", "Sample Description", ZonedDateTime.now(), 100, 50, 80, 500, true,
			"12346789",
			"John Doe", "Sample Publisher", null, null, null);

		when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

		// When
		bookImageServiceImpl.createBookImage(requestList);

		// Then
		verify(bookImageRepository, times(2)).save(any(BookImage.class));
	}

	@Test
	void createBookImage_withNullList_shouldDoNothing() {
		// When
		bookImageServiceImpl.createBookImage((List<CreateBookImageRequest>)null);

		// Then
		verify(bookImageRepository, never()).save(any(BookImage.class));
	}

	@Test
	void createBookImage_withEmptyList_shouldDoNothing() {
		// Given
		List<CreateBookImageRequest> requestList = Arrays.asList();

		// When
		bookImageServiceImpl.createBookImage(requestList);

		// Then
		verify(bookImageRepository, never()).save(any(BookImage.class));
	}

	@Test
	void createBookImage_withNonExistentBook_shouldThrowException() {
		// Given
		CreateBookImageRequest request = new CreateBookImageRequest("url1", BookImageType.MAIN, 1L);
		List<CreateBookImageRequest> requestList = Arrays.asList(request);

		when(bookRepository.findById(1L)).thenReturn(Optional.empty());

		// When / Then
		assertThrows(NotFindImageException.class, () -> bookImageServiceImpl.createBookImage(requestList));
	}

	@Test
	void createBookImage_withValidRequest_shouldSaveBookImage() {
		// Given
		CreateBookImageRequest request = new CreateBookImageRequest("url1", BookImageType.MAIN, 1L);

		Book book = new Book("Sample Book", "Sample Description", ZonedDateTime.now(), 100, 50, 80, 500, true,
			"12346789",
			"John Doe", "Sample Publisher", null, null, null);

		when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

		// When
		bookImageServiceImpl.createBookImage(request);

		// Then
		verify(bookImageRepository).save(any(BookImage.class));
	}

	@Test
	void createBookImage_withNonExistentBookInRequest_shouldThrowException() {
		// Given
		CreateBookImageRequest request = new CreateBookImageRequest("url1", BookImageType.MAIN, 1L);

		when(bookRepository.findById(1L)).thenReturn(Optional.empty());

		// When / Then
		assertThrows(NotFindImageException.class, () -> bookImageServiceImpl.createBookImage(request));
	}
}