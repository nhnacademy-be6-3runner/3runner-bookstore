package com.nhnacademy.bookstore.book.book.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.nhnacademy.bookstore.book.book.dto.response.AladinItem;
import com.nhnacademy.bookstore.book.book.dto.response.ApiCreateBookResponse;
import com.nhnacademy.bookstore.book.book.repository.ApiBookRepository;
import com.nhnacademy.bookstore.book.book.repository.BookRepository;
import com.nhnacademy.bookstore.book.bookCartegory.repository.BookCategoryRepository;
import com.nhnacademy.bookstore.book.bookImage.repository.BookImageRepository;
import com.nhnacademy.bookstore.book.category.exception.CategoryNotFoundException;
import com.nhnacademy.bookstore.book.category.repository.CategoryRepository;
import com.nhnacademy.bookstore.book.image.exception.MultipartFileException;
import com.nhnacademy.bookstore.book.image.imageService.ImageService;
import com.nhnacademy.bookstore.entity.book.Book;
import com.nhnacademy.bookstore.entity.bookCategory.BookCategory;
import com.nhnacademy.bookstore.entity.category.Category;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ApiBookServiceImplMockTest {

	@Mock
	private ApiBookRepository apiBookRepository;

	@Mock
	private BookRepository bookRepository;

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private BookCategoryRepository bookCategoryRepository;

	@Mock
	private ImageService imageService;

	@Mock
	private BookImageRepository bookImageRepository;

	@InjectMocks
	private ApiBookServiceImpl apiBookServiceImpl;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testRealAuthorName1() {
		// Given
		String author = "Test Author 지음";

		// When
		String result = apiBookServiceImpl.realAuthorName(author);

		// Then
		assertEquals("Test Author", result.trim());
	}

	@Test
	void testRealAuthorName2() {
		// Given
		String author = "Test Author 엮은이";

		// When
		String result = apiBookServiceImpl.realAuthorName(author);

		// Then
		assertEquals("Test Author", result.trim());
	}

	@Test
	void testRealAuthorName3() {
		// Given
		String author = "Test Author 옮김";

		// When
		String result = apiBookServiceImpl.realAuthorName(author);

		// Then
		assertEquals("Test Author", result.trim());
	}

	@Test
	void testRealAuthorName4() {
		// Given
		String author = "Test Author";

		// When
		String result = apiBookServiceImpl.realAuthorName(author);

		// Then
		assertEquals("Test Author", result.trim());
	}

	@Test
	void testStringToZonedDateTime() {
		// Given
		String dateStr = "2019-01-01";

		// When
		ZonedDateTime result = apiBookServiceImpl.stringToZonedDateTime(dateStr);

		// Then
		assertNotNull(result);
	}

	@Test
	void stringToZonedDateTimeNull() {
		ZonedDateTime result = apiBookServiceImpl.stringToZonedDateTime(null);
		assertNull(result);
	}

	@Test
	void testCategoryNameStringToList() {
		// Given
		String categoryName = "Category1>Category2>Category3";

		// When
		List<String> result = apiBookServiceImpl.categoryNameStringToList(categoryName);

		// Then
		assertEquals(Arrays.asList("Category1", "Category2", "Category3"), result);
	}

	@Test
	void downloadImageAsMultipartFile() {
		MultipartFile file = apiBookServiceImpl.downloadImageAsMultipartFile(
			"https://image.aladin.co.kr/product/34132/71/coversum/e712533508_1.jpg");
		assertNotNull(file);
	}

	@Test
	void downloadImageAsMultipartFileException() {
		assertThrows(MultipartFileException.class, () -> apiBookServiceImpl.downloadImageAsMultipartFile(
			"https://image.aladin.co.kr/product/34132/71/coversum234/e712533508_1.jpg"));

	}

	@Test
	void saveTest() {
		AladinItem item = AladinItem.builder()
			.title("Test Title")
			.author("Test Author")
			.description("Test Description")
			.isbn13("1234567890123")
			.priceSales(12500)
			.priceStandard(13000)
			.cover("https://image.aladin.co.kr/product/34132/71/coversum/e712533508_1.jpg")
			.categoryName("category1>category2>category3")
			.publisher("Test Publisher")
			.pubDate("2019-01-01")
			.build();

		ApiCreateBookResponse bookResponse = new ApiCreateBookResponse("Test Title",
			"http://www.aladin.co.kr/shop/wproduct.aspx?ISBN=334061481",
			List.of(item));

		Book book = new Book(
			bookResponse.title(),
			bookResponse.item().getFirst().description(),
			ZonedDateTime.now(),
			bookResponse.item().getFirst().priceSales(),
			100,
			bookResponse.item().getFirst().priceSales(),
			0,
			true,
			"Test Author",
			bookResponse.item().getFirst().isbn13(),
			bookResponse.item().getFirst().publisher(),
			null,
			null,
			null
		);

		Category category1 = new Category("category1");
		Optional<Category> categoryOptional = Optional.of(category1);
		BookCategory bookCategory = BookCategory.create(book, categoryOptional.get());

		when(apiBookRepository.getBookResponse(any())).thenReturn(bookResponse);
		when(categoryRepository.findByName(any())).thenReturn(Optional.of(category1));
		when(bookCategoryRepository.save(any(BookCategory.class))).thenReturn(bookCategory);
		when(bookRepository.save(any())).thenReturn(book);

		apiBookServiceImpl.save("12345678901231");

	}

	@Test
	void saveTestException() {
		AladinItem item = AladinItem.builder()
			.title("Test Title")
			.author("Test Author")
			.description("Test Description")
			.isbn13("1234567890123")
			.priceSales(12500)
			.priceStandard(13000)
			.cover("https://image.aladin.co.kr/product/34132/71/coversum/e712533508_1.jpg")
			.categoryName("category1>category2>category3")
			.publisher("Test Publisher")
			.pubDate("2019-01-01")
			.build();

		ApiCreateBookResponse bookResponse = new ApiCreateBookResponse("Test Title",
			"https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=334061481",
			List.of(item));

		Book book = new Book(
			bookResponse.title(),
			bookResponse.item().getFirst().description(),
			ZonedDateTime.now(),
			bookResponse.item().getFirst().priceSales(),
			100,
			bookResponse.item().getFirst().priceSales(),
			0,
			true,
			"Test Author",
			bookResponse.item().getFirst().isbn13(),
			bookResponse.item().getFirst().publisher(),
			null,
			null,
			null
		);

		when(apiBookRepository.getBookResponse(any())).thenReturn(bookResponse);
		when(categoryRepository.findByName(any())).thenReturn(Optional.empty());

		assertThrows(CategoryNotFoundException.class, () -> apiBookServiceImpl.save("1234567890123"));

	}
}
