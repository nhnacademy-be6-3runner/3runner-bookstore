package com.nhnacademy.bookstore.purchase.purchaseBook.dto.request;

import lombok.Builder;


/**
 * 주문-책 삭제 requestDto
 *
 * @author 정주혁
 * @param purchaseBookId
 */
@Builder
public record DeletePurchaseBookRequest(long purchaseBookId) {
}

