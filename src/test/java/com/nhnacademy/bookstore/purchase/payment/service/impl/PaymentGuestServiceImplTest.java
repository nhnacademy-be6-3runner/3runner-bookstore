package com.nhnacademy.bookstore.purchase.payment.service.impl;

import com.nhnacademy.bookstore.purchase.bookCart.dto.response.ReadBookCartGuestResponse;
import com.nhnacademy.bookstore.purchase.bookCart.service.BookCartGuestService;
import com.nhnacademy.bookstore.purchase.purchase.dto.request.CreatePurchaseRequest;
import com.nhnacademy.bookstore.purchase.purchase.service.PurchaseGuestService;
import com.nhnacademy.bookstore.purchase.purchaseBook.dto.request.CreatePurchaseBookRequest;
import com.nhnacademy.bookstore.purchase.purchaseBook.service.PurchaseBookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentGuestServiceImplTest {
    @Mock
    private BookCartGuestService bookCartGuestService;

    @Mock
    private PurchaseGuestService purchaseGuestService;

    @Mock
    private PurchaseBookService purchaseBookService;

    @InjectMocks
    private PaymentGuestServiceImpl paymentGuestService;

    @Test
    void payment() {
        Long cartId = 1L;
        String address = "Test Address";
        String password = "Test Password";
        Integer totalPrice = 10000;
        String orderId = "order123";

        // Mock data
        Long purchaseId = 1L;
        List<ReadBookCartGuestResponse> bookCartGuestResponseList = List.of(
                new ReadBookCartGuestResponse(1L, 1L, 5000, null, "Book 1", 2),
                new ReadBookCartGuestResponse(2L, 2L, 3000, null, "Book 2", 1)
        );

        when(purchaseGuestService.createPurchase(any(CreatePurchaseRequest.class))).thenReturn(purchaseId);
        when(bookCartGuestService.readAllBookCart(cartId)).thenReturn(bookCartGuestResponseList);

        // Call the method to test
        Long result = paymentGuestService.payment(cartId, address, password, totalPrice, orderId);

        // Verify interactions
        verify(purchaseGuestService, times(1)).createPurchase(any(CreatePurchaseRequest.class));
        verify(bookCartGuestService, times(1)).readAllBookCart(cartId);
        verify(purchaseBookService, times(2)).createPurchaseBook(any(CreatePurchaseBookRequest.class));

        // Assertions
        assertThat(result).isEqualTo(purchaseId);
    }

    @Test
    void refund() {
        Long result = paymentGuestService.refund();
        assertThat(result).isEqualTo(0L);
    }

    @Test
    void partialRefund() {
        Long result = paymentGuestService.partialRefund();
        assertThat(result).isEqualTo(0L);
    }
}