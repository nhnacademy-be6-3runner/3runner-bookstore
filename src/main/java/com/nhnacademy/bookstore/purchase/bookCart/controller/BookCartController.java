package com.nhnacademy.bookstore.purchase.bookCart.controller;

import com.nhnacademy.bookstore.purchase.bookCart.dto.request.*;
import com.nhnacademy.bookstore.purchase.bookCart.dto.response.ReadAllBookCartMemberResponse;
import com.nhnacademy.bookstore.purchase.bookCart.dto.response.ReadBookCartGuestResponse;
import com.nhnacademy.bookstore.purchase.bookCart.exception.BookCartArgumentErrorException;
import com.nhnacademy.bookstore.purchase.bookCart.service.BookCartGuestService;
import com.nhnacademy.bookstore.purchase.bookCart.service.BookCartMemberService;
import com.nhnacademy.bookstore.util.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * BookCart 비회원 컨트롤러.
 *
 * @author 김병우
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookstore/carts")
public class BookCartController {
    private final BookCartGuestService bookCartGuestService;
    private final BookCartMemberService bookCartMemberService;

    /**
     * 카트 목록 반환 api.
     *
     * @author 김병우
     * @param cartId 쿠키 요청
     * @return 카트 목록
     */
    @GetMapping("/{cartId}")
    public ApiResponse<List<ReadBookCartGuestResponse>> readCart(
            @PathVariable("cartId") Long cartId
    ) {
        List<ReadBookCartGuestResponse> responses = bookCartGuestService.readAllBookCart(cartId);
        return ApiResponse.success(responses);
    }

    @GetMapping()
    public ApiResponse<List<ReadAllBookCartMemberResponse>> readAllBookCartMember(
            @RequestHeader(name = "Member-Id") Long userId) {
        return ApiResponse.success(bookCartMemberService.readAllCartMember(ReadAllBookCartMemberRequest.builder().userId(userId).build()));
    }

    /**
     * 카트 추가 api
     *
     * @param createBookCartGuestRequest 카트 추가 폼
     * @param bindingResult 검증
     * @return api 응답
     */
    @PostMapping()
    public ApiResponse<Long> createCart(
            @Valid @RequestBody CreateBookCartRequest createBookCartGuestRequest,
            BindingResult bindingResult,
            @RequestHeader(value = "Member-Id", required = false) Long memberId
        ) {
        if (bindingResult.hasErrors()) {
            throw new BookCartArgumentErrorException("폼 에러");
        }

        if (Objects.isNull(memberId)) {
            Long cartId = bookCartGuestService.createBookCart(createBookCartGuestRequest.bookId(),
                    createBookCartGuestRequest.quantity());

            return ApiResponse.createSuccess(cartId);

        } else {
            createBookCartGuestRequest = CreateBookCartRequest.builder()
                    .bookId(createBookCartGuestRequest.bookId())
                    .quantity(createBookCartGuestRequest.quantity())
                    .userId(memberId)
                    .build();
            return ApiResponse.createSuccess(bookCartMemberService.createBookCartMember(createBookCartGuestRequest));
        }
    }

    /**
     * 카트 목록 수정 api.
     *
     * @param updateBookCartGuestRequest 수정 요청 폼
     * @param bindingResult 검증
     * @return api 응답
     */
    @PutMapping()
    public ApiResponse<Long> updateCart(
            @Valid @RequestBody UpdateBookCartRequest updateBookCartGuestRequest,
            BindingResult bindingResult,
            @RequestHeader(value = "Member-Id", required = false) Long memberId
    ) {

        if (bindingResult.hasErrors()) {
            throw new BookCartArgumentErrorException("폼 에러");
        }

        if (Objects.isNull(memberId)) {
            bookCartGuestService.updateBookCart(
                updateBookCartGuestRequest.bookId(),
                updateBookCartGuestRequest.cartId(),
                updateBookCartGuestRequest.quantity()
            );
            return ApiResponse.success(updateBookCartGuestRequest.cartId());
        } else {
            return ApiResponse.success(bookCartMemberService.updateBookCartMember(updateBookCartGuestRequest));
        }

    }

    @DeleteMapping()
    public ApiResponse<Long> deleteCart(
            @Valid @RequestBody DeleteBookCartRequest deleteBookCartGuestRequest,
            BindingResult bindingResult,
            @RequestHeader(value = "Member-Id", required = false) Long memberId
    ) {
        if (bindingResult.hasErrors()) {
            throw new BookCartArgumentErrorException("폼 에러");
        }

        if (Objects.isNull(memberId)) {
            bookCartGuestService.deleteBookCart(
                    deleteBookCartGuestRequest.bookCartId(),
                    deleteBookCartGuestRequest.cartId());

            return ApiResponse.deleteSuccess(deleteBookCartGuestRequest.cartId());
        } else {

            return ApiResponse.deleteSuccess(bookCartMemberService.deleteBookCartMember(deleteBookCartGuestRequest));
        }
    }
}
