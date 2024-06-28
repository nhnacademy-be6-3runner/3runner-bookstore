package com.nhnacademy.bookstore.purchase.payment.service.impl;

import com.nhnacademy.bookstore.purchase.bookCart.dto.request.ReadAllBookCartMemberRequest;
import com.nhnacademy.bookstore.purchase.bookCart.dto.response.ReadAllBookCartMemberResponse;
import com.nhnacademy.bookstore.purchase.bookCart.service.BookCartMemberService;
import com.nhnacademy.bookstore.purchase.payment.service.PaymentMemberService;
import com.nhnacademy.bookstore.purchase.purchase.dto.request.CreatePurchaseRequest;
import com.nhnacademy.bookstore.purchase.purchase.service.PurchaseMemberService;
import com.nhnacademy.bookstore.purchase.purchaseBook.dto.request.CreatePurchaseBookRequest;
import com.nhnacademy.bookstore.purchase.purchaseBook.service.PurchaseBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class PaymentMemberServiceImpl implements PaymentMemberService {
    private final BookCartMemberService bookCartMemberService;
    private final PurchaseMemberService purchaseMemberService;
    private final PurchaseBookService purchaseBookService;

    @Override
    public Long payment(Long memberId, String address, Integer totalPrice, String orderId) {
        Long purchaseId = purchaseMemberService.createPurchase(
                CreatePurchaseRequest.builder()
                        .orderId(orderId)
                        .road(address)
                        .totalPrice(totalPrice)
                        .deliveryPrice(3000).build(),
                memberId
        );

        List<ReadAllBookCartMemberResponse> bookCartMemberResponseList = bookCartMemberService.readAllCartMember(ReadAllBookCartMemberRequest.builder().userId(memberId).build());

        for (ReadAllBookCartMemberResponse bookCartMemberResponse : bookCartMemberResponseList) {
            purchaseBookService.createPurchaseBook(
                    CreatePurchaseBookRequest.builder()
                            .bookId(bookCartMemberResponse.bookId())
                            .purchaseId(purchaseId)
                            .price(bookCartMemberResponse.price())
                            .quantity(bookCartMemberResponse.quantity())
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
