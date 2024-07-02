package com.nhnacademy.bookstore.purchase.payment.service.impl;

import com.nhnacademy.bookstore.purchase.bookCart.dto.request.ReadAllBookCartMemberRequest;
import com.nhnacademy.bookstore.purchase.bookCart.dto.response.ReadAllBookCartMemberResponse;
import com.nhnacademy.bookstore.purchase.bookCart.service.BookCartMemberService;
import com.nhnacademy.bookstore.purchase.purchase.dto.request.CreatePurchaseRequest;
import com.nhnacademy.bookstore.purchase.purchase.service.PurchaseMemberService;
import com.nhnacademy.bookstore.purchase.purchaseBook.dto.request.CreatePurchaseBookRequest;
import com.nhnacademy.bookstore.purchase.purchaseBook.service.PurchaseBookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentMemberServiceImplTest {

    @Mock
    private BookCartMemberService bookCartMemberService;

    @Mock
    private PurchaseMemberService purchaseMemberService;

    @Mock
    private PurchaseBookService purchaseBookService;

    @InjectMocks
    private PaymentMemberServiceImpl paymentMemberService;

    @Test
    void payment() {
        Long memberId = 1L;
        String address = "Test Address";
        Integer totalPrice = 10000;
        String orderId = "order123";

        // Mock data
        Long purchaseId = 1L;
        List<ReadAllBookCartMemberResponse> bookCartMemberResponseList = List.of(
                new ReadAllBookCartMemberResponse(1L, 1L, 5000, null, "Book 1", 2),
                new ReadAllBookCartMemberResponse(2L, 2L, 3000, null, "Book 2", 1)
        );

        when(purchaseMemberService.createPurchase(any(CreatePurchaseRequest.class), eq(memberId))).thenReturn(purchaseId);
        when(bookCartMemberService.readAllCartMember(any(ReadAllBookCartMemberRequest.class))).thenReturn(bookCartMemberResponseList);

        // Call the method to test
        Long result = paymentMemberService.payment(memberId, address, totalPrice, orderId);

        // Verify interactions
        verify(purchaseMemberService, times(1)).createPurchase(any(CreatePurchaseRequest.class), eq(memberId));
        verify(bookCartMemberService, times(1)).readAllCartMember(any(ReadAllBookCartMemberRequest.class));
        verify(purchaseBookService, times(2)).createPurchaseBook(any(CreatePurchaseBookRequest.class));

        // Assertions
        assertThat(result).isEqualTo(purchaseId);
    }

    @Test
    void refund() {
        Long result = paymentMemberService.refund();
        assertThat(result).isZero();
    }

    @Test
    void partialRefund() {
        Long result = paymentMemberService.partialRefund();
        assertThat(result).isZero();
    }
}