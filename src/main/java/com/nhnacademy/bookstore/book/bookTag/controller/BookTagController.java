package com.nhnacademy.bookstore.book.bookTag.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstore.book.bookTag.dto.request.ReadBookIdRequest;
import com.nhnacademy.bookstore.book.bookTag.dto.request.ReadTagRequest;
import com.nhnacademy.bookstore.book.bookTag.dto.response.ReadBookByTagResponse;
import com.nhnacademy.bookstore.book.bookTag.dto.response.ReadTagByBookResponse;
import com.nhnacademy.bookstore.book.bookTag.exception.ReadBookTagNotFoundResponseException;
import com.nhnacademy.bookstore.book.bookTag.exception.ReadBookTagRequestFormException;
import com.nhnacademy.bookstore.book.bookTag.service.BookTagService;
import com.nhnacademy.bookstore.util.ApiResponse;

import jakarta.validation.Valid;

/**
 * @author 정주혁
 *
 * booktagController
 */

@RestController
@RequestMapping("/bookstore/booktags")
public class BookTagController {

	@Autowired
	private BookTagService bookTagService;

	/**
	 * 태그값으로 책들을 가져오기 위한 메소드
	 *
	 * @param tagId 해당 태그가 달린 책을 가져오기 위한 태그 id, page를 설정할 size, 현재 page, 정렬할 sort 를 포함
	 *
	 * @return ApiResponse<Page < ReadBookByTagResponse>> 커스터마이징 한 헤더와 불러온 해당 태그가 달린 책들로 이루어진 바디를 합친 실행 값
	 */
	@GetMapping("/{tagId}/books")
	public ApiResponse<Page<ReadBookByTagResponse>> readBookByTagId(@Valid @RequestBody ReadTagRequest tagId,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {

			throw new ReadBookTagRequestFormException(bindingResult.getFieldErrors().toString());
		}

		Pageable pageable;
		if (!tagId.sort().isEmpty()) {
			pageable = PageRequest.of(tagId.page(), tagId.page(), Sort.by(tagId.sort()));
		} else {
			pageable = PageRequest.of(tagId.page(), tagId.size());
		}

		Page<ReadBookByTagResponse> bookByTagResponsePage = bookTagService.readBookByTagId(tagId, pageable);
		if (bookByTagResponsePage.isEmpty()) {

			throw new ReadBookTagNotFoundResponseException("책을 찾을수가 없습니다.");
		}

		return ApiResponse.success(bookByTagResponsePage);
	}

	/**
	 * 책에 달린 태그들을 가져오기 위한 메소드
	 * @param bookId 책에 달린 태그들을 가져오기 위한 책 id
	 * @return ApiResponse<Page < ReadBookByTagResponse>> 커스터마이징 한 헤더와 불러온 해당 책에 달린 태그들로 이루어진 바디를 합친 실행 값
	 */
	@GetMapping("/{bookId}/tags")
	public ApiResponse<List<ReadTagByBookResponse>> readTagByBookId(@Valid @RequestBody ReadBookIdRequest bookId,
		BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {

			throw new ReadBookTagRequestFormException(bindingResult.getFieldErrors().toString());
		}
		List<ReadTagByBookResponse> tags = bookTagService.readTagByBookId(bookId);
		if (tags.isEmpty()) {
			throw new ReadBookTagNotFoundResponseException("Tag가 없습니다.");
		}

		return ApiResponse.success(tags);

	}
}
