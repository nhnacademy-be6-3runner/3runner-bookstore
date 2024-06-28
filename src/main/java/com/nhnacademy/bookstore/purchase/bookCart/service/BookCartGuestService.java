package com.nhnacademy.bookstore.purchase.bookCart.service;

import com.nhnacademy.bookstore.purchase.bookCart.dto.response.ReadBookCartGuestResponse;
import java.util.List;

/**
 * 도서장바구니 서비스 인터페이스.
 *
 * @author 김병우
 */
public interface BookCartGuestService {

    Long createBookCart(Long bookId, int quantity);

    Long updateBookCart(Long bookId, Long cartId, int quantity);

    List<ReadBookCartGuestResponse> readAllBookCart(Long cartId);

    Long deleteBookCart(Long bookCartId, Long cartId);

    Long deleteAllBookCart(Long cartId);
}
