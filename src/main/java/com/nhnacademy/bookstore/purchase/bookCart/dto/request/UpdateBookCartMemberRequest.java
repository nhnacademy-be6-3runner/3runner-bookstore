package com.nhnacademy.bookstore.purchase.bookCart.dto.request;

import com.nhnacademy.bookstore.entity.book.Book;
import com.nhnacademy.bookstore.entity.cart.Cart;

import lombok.Builder;

@Builder
public record UpdateBookCartMemberRequest( int quantity, long bookId, long cartId) {
}
