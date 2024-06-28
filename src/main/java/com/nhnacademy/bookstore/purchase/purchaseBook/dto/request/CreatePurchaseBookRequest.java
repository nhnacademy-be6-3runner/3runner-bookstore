package com.nhnacademy.bookstore.purchase.purchaseBook.dto.request;

import com.nhnacademy.bookstore.entity.book.Book;
import com.nhnacademy.bookstore.entity.purchase.Purchase;
import lombok.Builder;

/**
 * 주문-책 생성 requestDto
 *
 * @author 정주혁
 *
 * @param bookId
 * @param quantity
 * @param price
 * @param purchaseId
 */

@Builder
public record CreatePurchaseBookRequest(long bookId, int quantity, int price, long purchaseId) {
}
