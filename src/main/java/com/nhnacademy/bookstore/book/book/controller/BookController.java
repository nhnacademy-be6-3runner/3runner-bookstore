package com.nhnacademy.bookstore.book.book.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstore.book.book.dto.request.CreateBookRequest;
import com.nhnacademy.bookstore.book.book.dto.response.ReadBookResponse;
import com.nhnacademy.bookstore.book.book.dto.response.UserReadBookResponse;
import com.nhnacademy.bookstore.book.book.exception.CreateBookRequestFormException;
import com.nhnacademy.bookstore.book.book.service.BookService;
import com.nhnacademy.bookstore.book.bookCartegory.dto.request.CreateBookCategoryRequest;
import com.nhnacademy.bookstore.book.bookCartegory.service.BookCategoryService;
import com.nhnacademy.bookstore.book.bookImage.service.BookImageService;
import com.nhnacademy.bookstore.book.bookTag.dto.request.CreateBookTagListRequest;
import com.nhnacademy.bookstore.book.bookTag.dto.request.ReadBookIdRequest;
import com.nhnacademy.bookstore.book.bookTag.dto.response.ReadTagByBookResponse;
import com.nhnacademy.bookstore.book.bookTag.service.BookTagService;
import com.nhnacademy.bookstore.book.category.dto.response.CategoryParentWithChildrenResponse;
import com.nhnacademy.bookstore.entity.bookImage.enums.BookImageType;
import com.nhnacademy.bookstore.util.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 책 요청 컨트롤러.
 *
 * @author 김병우
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookstore/books")
@Slf4j
public class BookController {
	private final BookService bookService;
	private final BookImageService bookImageService;
	private final BookTagService bookTagService;
	private final BookCategoryService bookCategoryService;

	/**
	 * 책 등록 요청 처리.
	 *
	 * @param createBookRequest request form
	 * @param bindingResult binding result
	 * @return ApiResponse<>
	 */
	@Transactional
	@PostMapping
	public ApiResponse<Void> createBook(@Valid @RequestBody CreateBookRequest createBookRequest,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new CreateBookRequestFormException(bindingResult.getFieldErrors().toString());
		}
		long bookId = bookService.createBook(createBookRequest);
		bookCategoryService.createBookCategory(
			CreateBookCategoryRequest.builder().bookId(bookId).categoryIds(createBookRequest.categoryIds()).build());
		bookTagService.createBookTag(
			CreateBookTagListRequest.builder().bookId(bookId).tagIdList(createBookRequest.tagIds()).build());
		bookImageService.createBookImage(createBookRequest.imageList(), bookId, BookImageType.DESCRIPTION);
		if (!Objects.isNull(createBookRequest.imageName())) {
			bookImageService.createBookImage(List.of(createBookRequest.imageName()), bookId, BookImageType.MAIN);
		}

		return new ApiResponse<>(new ApiResponse.Header(true, 201));
	}

	@GetMapping("/{bookId}")
	public ApiResponse<UserReadBookResponse> readBook(@PathVariable("bookId") Long bookId) {
		ReadBookResponse detailBook = bookService.readBookById(bookId);
		List<CategoryParentWithChildrenResponse> categoryList = bookCategoryService.readBookWithCategoryList(bookId);
		List<ReadTagByBookResponse> tagList =
			bookTagService.readTagByBookId(
				ReadBookIdRequest.builder().bookId(bookId).build());

		UserReadBookResponse book = UserReadBookResponse.builder()
			.id(detailBook.id())
			.title(detailBook.title())
			.description(detailBook.description())
			.publishedDate(detailBook.publishedDate())
			.price(detailBook.price())
			.quantity(detailBook.quantity())
			.sellingPrice(detailBook.sellingPrice())
			.viewCount(detailBook.viewCount())
			.packing(detailBook.packing())
			.author(detailBook.author())
			.isbn(detailBook.isbn())
			.publisher(detailBook.publisher())
			.imagePath(detailBook.imagePath())
			.categoryList(categoryList)
			.tagList(tagList)
			.build();

		return ApiResponse.success(book);
	}
}

/**
 *
 * 비트코인 창시자인 사토시 나카모토가 비트코인 출시 후 기반을 다지던 2년여 동안 주고받은 이메일과 포럼에 남긴 게시물을 담았다. 비트코인과 제작자의 사고방식이 궁금하다면 매우 흥미로울 것이다. 컴퓨터 소프트웨어 배경지식이 있는 사람이 쉽게 읽을 수 있도록 구성되었고, 글 일부는 경제학적 개념을 담고 있어 정보 기술에 대한 배경지식이 없는 경제학자나 투자자도 관심 있게 볼 수 있다.
 *
 *
 * ![image alt attribute](/api/images/book/download?fileName=5f5fc4f4342911ef9c491598c408ca79.jpg)
 */