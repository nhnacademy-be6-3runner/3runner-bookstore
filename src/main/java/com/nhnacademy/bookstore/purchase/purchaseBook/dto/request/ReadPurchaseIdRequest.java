package com.nhnacademy.bookstore.purchase.purchaseBook.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;


/**
 * 주문-책 조회 requestDto
 *
 * @author 정주혁
 *
 * @param purchaseId
 */
@Builder
public record ReadPurchaseIdRequest(@NotNull Long purchaseId,
									@NotNull int page,
									@NotNull int size,
									String sort) {
}
