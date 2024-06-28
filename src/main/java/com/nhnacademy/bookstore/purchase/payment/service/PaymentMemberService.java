package com.nhnacademy.bookstore.purchase.payment.service;

public interface PaymentMemberService {
    Long payment(Long memberId, String address, Integer totalPrice, String orderId);
    Long refund();
    Long partialRefund();
}
