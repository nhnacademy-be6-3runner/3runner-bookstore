package com.nhnacademy.bookstore.purchase.bookCart.controller;

import com.nhnacademy.bookstore.purchase.bookCart.dto.request.CreateBookCartMemberRequest;
import com.nhnacademy.bookstore.purchase.bookCart.dto.request.DeleteBookCartMemberRequest;
import com.nhnacademy.bookstore.purchase.bookCart.dto.request.ReadAllBookCartMemberRequest;
import com.nhnacademy.bookstore.purchase.bookCart.dto.request.UpdateBookCartMemberRequest;
import com.nhnacademy.bookstore.purchase.bookCart.dto.response.ReadAllBookCartMemberResponse;
import com.nhnacademy.bookstore.purchase.bookCart.exception.CreateBookCartMemberRequestFormException;
import com.nhnacademy.bookstore.purchase.bookCart.exception.DeleteBookCartMemberRequestFormException;
import com.nhnacademy.bookstore.purchase.bookCart.exception.UpdateBookCartMemberRequestFormException;
import com.nhnacademy.bookstore.purchase.bookCart.service.BookCartMemberService;
import com.nhnacademy.bookstore.util.ApiResponse;
import com.nhnacademy.bookstore.util.ValidationUtils;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 book-cart controller
 *
 * @author 정주혁
 */
@RestController
@RequestMapping("/bookstore/cart")
@RequiredArgsConstructor
public class BookCartMemberController {
	private final BookCartMemberService bookCartMemberService;

	@PostMapping
	public ApiResponse<Long> createBookCartMember(@RequestBody @Valid CreateBookCartMemberRequest request,@RequestHeader(name = "Member-Id") Long userId, BindingResult bindingResult) {
		ValidationUtils.validateBindingResult(bindingResult, new CreateBookCartMemberRequestFormException());
		request = CreateBookCartMemberRequest.builder()
			.bookId(request.bookId())
			.quantity(request.quantity())
			.userId(userId)
			.build();
		return ApiResponse.createSuccess(bookCartMemberService.createBookCartMember(request));
	}

	@GetMapping
	public ApiResponse<List<ReadAllBookCartMemberResponse>> readAllBookCartMember(
		@RequestHeader(name = "Member-Id") Long userId) {
		return ApiResponse.success(bookCartMemberService.readAllCartMember(ReadAllBookCartMemberRequest.builder().userId(userId).build()));
	}

	@PutMapping
	public ApiResponse<Long> updateBookCartMember(@RequestBody @Valid UpdateBookCartMemberRequest request, BindingResult bindingResult) {
		ValidationUtils.validateBindingResult(bindingResult,new UpdateBookCartMemberRequestFormException());
		return ApiResponse.success(bookCartMemberService.updateBookCartMember(request));
	}

	@DeleteMapping
	public ApiResponse<Void> deleteBookCartMember(@RequestBody @Valid DeleteBookCartMemberRequest request, BindingResult bindingResult) {
		ValidationUtils.validateBindingResult(bindingResult, new DeleteBookCartMemberRequestFormException());
		bookCartMemberService.deleteBookCartMember(request);
		return ApiResponse.deleteSuccess(null);
	}
}
