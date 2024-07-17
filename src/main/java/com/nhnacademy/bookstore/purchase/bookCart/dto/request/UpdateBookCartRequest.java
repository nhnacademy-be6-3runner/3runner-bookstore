package com.nhnacademy.bookstore.purchase.bookCart.dto.request;


import jakarta.validation.constraints.Min;
import lombok.Builder;


/**
 * 북카트 업데이트 요청.
 *
 * @param cartId
 * @param bookId
 * @param quantity
 */
@Builder
public record UpdateBookCartRequest(
        @Min(0) long cartId,
        @Min(1) long bookId,
        @Min(1) int quantity) {
}
