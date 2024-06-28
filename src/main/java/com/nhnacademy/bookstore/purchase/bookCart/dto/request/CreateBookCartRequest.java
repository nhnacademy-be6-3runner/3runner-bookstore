package com.nhnacademy.bookstore.purchase.bookCart.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

/**
 * 카트 생성 폼.
 *
 * @author 김병우
 * @param bookId 도서아이디
 * @param quantity 도서수량
 */

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CreateBookCartRequest(
        long bookId,
        long userId,
        int quantity) {
    }
