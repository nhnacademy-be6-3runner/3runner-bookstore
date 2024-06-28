package com.nhnacademy.bookstore.purchase.bookCart.dto.request;

import lombok.Builder;

@Builder
public record DeleteBookCartRequest(
        long cartId,
        long bookCartId) {
}
