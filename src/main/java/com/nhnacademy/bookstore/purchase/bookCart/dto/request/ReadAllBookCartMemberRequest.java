package com.nhnacademy.bookstore.purchase.bookCart.dto.request;

import lombok.Builder;

@Builder
public record ReadAllBookCartMemberRequest(long userId) {

}
