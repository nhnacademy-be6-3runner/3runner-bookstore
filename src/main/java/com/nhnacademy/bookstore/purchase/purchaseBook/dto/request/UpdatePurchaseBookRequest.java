package com.nhnacademy.bookstore.purchase.purchaseBook.dto.request;

import com.nhnacademy.bookstore.entity.book.Book;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;


/**
 * 주문-책 수정 requestDto
 *
 * @author 정주혁
 *
 * @param bookId
 * @param quantity
 * @param price
 * @param purchaseId
 */
@Builder
public record UpdatePurchaseBookRequest(long bookId, @NotNull @Min(0)int quantity, @NotNull @Min(0) int price, long purchaseId ) {
}
