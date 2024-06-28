package com.nhnacademy.bookstore.book.bookTag.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstore.book.book.exception.BookDoesNotExistException;
import com.nhnacademy.bookstore.book.book.repository.BookRepository;
import com.nhnacademy.bookstore.book.bookTag.dto.request.CreateBookTagListRequest;
import com.nhnacademy.bookstore.book.bookTag.dto.request.CreateBookTagRequest;
import com.nhnacademy.bookstore.book.bookTag.dto.request.ReadBookIdRequest;
import com.nhnacademy.bookstore.book.bookTag.dto.request.ReadTagRequest;
import com.nhnacademy.bookstore.book.bookTag.dto.response.ReadBookByTagResponse;
import com.nhnacademy.bookstore.book.bookTag.dto.response.ReadTagByBookResponse;
import com.nhnacademy.bookstore.book.bookTag.exception.AlreadyExistsBookTagException;
import com.nhnacademy.bookstore.book.bookTag.exception.NotExistsBookTagException;
import com.nhnacademy.bookstore.book.bookTag.repository.BookTagRepository;
import com.nhnacademy.bookstore.book.bookTag.service.Impl.BookTagServiceImpl;
import com.nhnacademy.bookstore.book.tag.repository.TagRepository;
import com.nhnacademy.bookstore.entity.book.Book;
import com.nhnacademy.bookstore.entity.bookTag.BookTag;
import com.nhnacademy.bookstore.entity.tag.Tag;

class BookTagServiceImplTest {

	@Mock
	private BookTagRepository bookTagRepository;

	@Mock
	private BookRepository bookRepository;

	@Mock
	private TagRepository tagRepository;

	@InjectMocks
	private BookTagServiceImpl bookTagService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void readBookByTagId_Success() {
		// Mocking input
		ReadTagRequest tagRequest = new ReadTagRequest(1L, 0, 10, "title");
		Pageable pageable = PageRequest.of(tagRequest.page(), tagRequest.size());

		// Mocking repository response
		Page<Book> mockPage = new PageImpl<>(Collections.singletonList(
			new Book("Sample Book", "Sample Description", ZonedDateTime.now(),
				100, 50, 80, 500, true, "John Doe", "1234567789", "Sample Publisher", null, null, null)
		));
		when(bookTagRepository.findAllBookIdByTagId(anyLong(), any())).thenReturn(mockPage);

		// Calling the service method
		Page<ReadBookByTagResponse> response = bookTagService.readBookByTagId(tagRequest, pageable);

		// Verifying repository method invocation
		verify(bookTagRepository, times(1)).findAllBookIdByTagId(anyLong(), any());

		// Assertions
		assertFalse(response.isEmpty());
		assertEquals(1, response.getContent().size());
		assertEquals("Sample Book", response.getContent().getFirst().title());
	}

	@Test
	void readTagByBookId_Success() {
		// Mocking input
		ReadBookIdRequest bookIdRequest = new ReadBookIdRequest(1L);
		Tag t = new Tag();
		t.setName("Fantasy");
		// Mocking repository response
		List<Tag> mockSet = new ArrayList<>();
		mockSet.add(t);
		when(bookTagRepository.findAllTagIdByBookId(anyLong())).thenReturn(mockSet);

		// Calling the service method
		List<ReadTagByBookResponse> response = bookTagService.readTagByBookId(bookIdRequest);

		// Verifying repository method invocation
		verify(bookTagRepository, times(1)).findAllTagIdByBookId(anyLong());

		// Assertions
		assertFalse(response.isEmpty());
		assertEquals(1, response.size());
		assertEquals("Fantasy", response.iterator().next().name());
	}

	@Test
	void createBookTag_Success() {
		CreateBookTagRequest bookTagRequest = CreateBookTagRequest.builder()
			.bookId(1L).tagId(1L).build();

		when(bookTagRepository.existsByBookIdAndTagId(1L, 1L)).thenReturn(false);
		when(bookRepository.existsById(1L)).thenReturn(true);
		when(tagRepository.existsById(1L)).thenReturn(true);
		when(bookTagRepository.save(any(BookTag.class))).thenReturn(new BookTag());

		Long id = bookTagService.createBookTag(bookTagRequest);

		assertNotNull(id);
	}

	@Test
	void createBookTag_Fail_1() {
		CreateBookTagRequest bookTagRequest = CreateBookTagRequest.builder()
			.bookId(1L).tagId(1L).build();

		when(bookTagRepository.existsByBookIdAndTagId(1L, 1L)).thenReturn(true);

		assertThrows(AlreadyExistsBookTagException.class, () -> bookTagService.createBookTag(bookTagRequest));
	}

	@Test
	void createBookTag_Fail_2() {
		CreateBookTagRequest bookTagRequest = CreateBookTagRequest.builder()
			.bookId(1L).tagId(1L).build();

		when(bookTagRepository.existsByBookIdAndTagId(1L, 1L)).thenReturn(false);
		when(bookRepository.existsById(1L)).thenReturn(false);

		assertThrows(BookDoesNotExistException.class, () -> bookTagService.createBookTag(bookTagRequest));
	}

	@Test
	void createBookTag_Fail_3() {
		CreateBookTagRequest bookTagRequest = CreateBookTagRequest.builder()
			.bookId(1L).tagId(1L).build();

		when(bookTagRepository.existsByBookIdAndTagId(1L, 1L)).thenReturn(false);
		when(bookRepository.existsById(1L)).thenReturn(true);
		when(tagRepository.existsById(1L)).thenReturn(false);

		assertThrows(NotExistsBookTagException.class, () -> bookTagService.createBookTag(bookTagRequest));
	}

	@Test
	void createBookTagList_Success() {
		CreateBookTagListRequest createBookTagListRequest = CreateBookTagListRequest.builder()
			.bookId(1L)
			.tagIdList(List.of(1L, 2L))
			.build();

		when(bookTagRepository.existsByBookIdAndTagId(1L, 1L)).thenReturn(false);
		when(bookTagRepository.existsByBookIdAndTagId(1L, 2L)).thenReturn(false);

		when(bookRepository.existsById(anyLong())).thenReturn(true);
		when(tagRepository.existsById(anyLong())).thenReturn(true);
		when(bookTagRepository.save(any(BookTag.class))).thenReturn(new BookTag());

		bookTagService.createBookTag(createBookTagListRequest);

		verify(bookTagRepository, times(1)).existsByBookIdAndTagId(1L, 1L);
		verify(bookTagRepository, times(1)).existsByBookIdAndTagId(1L, 2L);
	}

	@Test
	void createBookTagList_Fail_1() {
		CreateBookTagListRequest createBookTagListRequest = CreateBookTagListRequest.builder()
			.bookId(1L)
			.tagIdList(List.of(1L, 2L))
			.build();

		when(bookTagRepository.existsByBookIdAndTagId(1L, 1L)).thenReturn(false);

		when(bookRepository.existsById(anyLong())).thenReturn(false);

		assertThrows(BookDoesNotExistException.class, () -> bookTagService.createBookTag(createBookTagListRequest));
	}

	@Test
	void createBookTagList_Fail_2() {
		CreateBookTagListRequest createBookTagListRequest = CreateBookTagListRequest.builder()
			.bookId(1L)
			.tagIdList(List.of(1L, 2L))
			.build();

		when(bookTagRepository.existsByBookIdAndTagId(1L, 1L)).thenReturn(true);

		when(bookRepository.existsById(anyLong())).thenReturn(true);

		assertThrows(AlreadyExistsBookTagException.class, () -> bookTagService.createBookTag(createBookTagListRequest));
	}

	@Test
	void createBookTagList_Fail_3() {
		CreateBookTagListRequest createBookTagListRequest = CreateBookTagListRequest.builder()
			.bookId(1L)
			.tagIdList(List.of(1L, 2L))
			.build();

		when(bookTagRepository.existsByBookIdAndTagId(1L, 1L)).thenReturn(false);

		when(bookRepository.existsById(anyLong())).thenReturn(true);
		when(tagRepository.existsById(anyLong())).thenReturn(false);

		assertThrows(NotExistsBookTagException.class, () -> bookTagService.createBookTag(createBookTagListRequest));
	}
}
