package com.nhnacademy.bookstore.purchase.payment.service;

public interface PaymentGuestService {
    Long payment(Long cartId, String address, String password, Integer totalPrice, String orderId);
    Long refund();
    Long partialRefund();
}
