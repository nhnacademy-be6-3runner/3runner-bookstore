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
        long cartId,
        long bookId,
        int quantity) {
}
