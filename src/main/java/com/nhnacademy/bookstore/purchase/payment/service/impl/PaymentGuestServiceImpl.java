package com.nhnacademy.bookstore.purchase.payment.service.impl;

import com.nhnacademy.bookstore.purchase.bookCart.dto.response.ReadBookCartGuestResponse;
import com.nhnacademy.bookstore.purchase.bookCart.service.BookCartGuestService;
import com.nhnacademy.bookstore.purchase.payment.service.PaymentGuestService;
import com.nhnacademy.bookstore.purchase.purchase.dto.request.CreatePurchaseRequest;
import com.nhnacademy.bookstore.purchase.purchase.service.PurchaseGuestService;
import com.nhnacademy.bookstore.purchase.purchaseBook.dto.request.CreatePurchaseBookRequest;
import com.nhnacademy.bookstore.purchase.purchaseBook.service.PurchaseBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class PaymentGuestServiceImpl implements PaymentGuestService {
    private final BookCartGuestService bookCartGuestService;
    private final PurchaseGuestService purchaseGuestService;
    private final PurchaseBookService purchaseBookService;

    //북 카트 -> 삭제
    //북 주문 -> 주문
    //주문 추가
    // 포인트, 쿠폰 사용여부
    //@Transactional(propagation = Propagation.MANDATORY) 서비스생성후 한번에 사용
    @Override
    public Long payment(Long cartId, String address, String password, Integer totalPrice, String orderId) {
        Long purchaseId = purchaseGuestService.createPurchase(
                CreatePurchaseRequest.builder()
                        .orderId(orderId)
                        .road(address)
                        .password(password)
                        .totalPrice(totalPrice)
                        .deliveryPrice(3000).build()
        );

        List<ReadBookCartGuestResponse> bookCartGuestResponseList = bookCartGuestService.readAllBookCart(cartId);

        for (ReadBookCartGuestResponse bookCartGuestResponse : bookCartGuestResponseList) {
            purchaseBookService.createPurchaseBook(
                    CreatePurchaseBookRequest.builder()
                            .bookId(bookCartGuestResponse.bookId())
                            .purchaseId(purchaseId)
                            .price(bookCartGuestResponse.price())
                            .quantity(bookCartGuestResponse.quantity())
                            .build()
            );
        }
        return purchaseId;
    }

    @Override
    public Long refund() {
        return 0L;
    }

    @Override
    public Long partialRefund() {
        return 0L;
    }
}
