package com.nhnacademy.bookstore.purchase.purchase.service.impl;

import com.nhnacademy.bookstore.entity.purchase.Purchase;
import com.nhnacademy.bookstore.entity.purchase.enums.MemberType;
import com.nhnacademy.bookstore.entity.purchase.enums.PurchaseStatus;
import com.nhnacademy.bookstore.purchase.purchase.dto.request.CreatePurchaseRequest;
import com.nhnacademy.bookstore.purchase.purchase.dto.request.UpdatePurchaseGuestRequest;
import com.nhnacademy.bookstore.purchase.purchase.dto.response.ReadPurchaseResponse;
import com.nhnacademy.bookstore.purchase.purchase.exception.PurchaseAlreadyExistException;
import com.nhnacademy.bookstore.purchase.purchase.exception.PurchaseDoesNotExistException;
import com.nhnacademy.bookstore.purchase.purchase.exception.PurchaseNoAuthorizationException;
import com.nhnacademy.bookstore.purchase.purchase.repository.PurchaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseGuestServiceImplTest {
    private static final Logger log = LoggerFactory.getLogger(PurchaseGuestServiceImplTest.class);
    @Mock
    private PurchaseRepository purchaseRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private PurchaseGuestServiceImpl purchaseGuestService;

    private Purchase purchase;
    private CreatePurchaseRequest request;
    private UpdatePurchaseGuestRequest updateRequest;

    @BeforeEach
    void setUp() {
        purchase = new Purchase(UUID.randomUUID(), PurchaseStatus.SHIPPED, 100, 10, ZonedDateTime.now(), "road", "password", MemberType.NONMEMBER, null,null,null,null);

        request = CreatePurchaseRequest.builder().orderId(UUID.randomUUID().toString()).deliveryPrice(100).totalPrice(1000).road("dfdfd").password("abcdefg").build();
        updateRequest = UpdatePurchaseGuestRequest.builder().purchaseStatus(PurchaseStatus.SHIPPED).orderNumber(UUID.randomUUID()).password("password").build();
    }

    @Test
    void createPurchase() {
        when(purchaseRepository.existsPurchaseByOrderNumber(any(UUID.class))).thenReturn(false);
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(purchase);

        Long purchaseId = purchaseGuestService.createPurchase(request);

        assertNotNull(purchaseId);
        verify(purchaseRepository, times(1)).save(any(Purchase.class));
    }

    @Test
    void createPurchase_PurchaseAlreadyExist() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(purchaseRepository.existsPurchaseByOrderNumber(any(UUID.class))).thenReturn(true);

        assertThrows(PurchaseAlreadyExistException.class, () -> purchaseGuestService.createPurchase(request));
    }

    @Test
    void updatePurchase() {

        when(purchaseRepository.findPurchaseByOrderNumber(any(UUID.class))).thenReturn(Optional.of(purchase));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        Long purchaseId = purchaseGuestService.updatePurchase(updateRequest);

        assertNotNull(purchaseId);
        assertEquals(purchase.getId(), purchaseId);
        assertEquals(PurchaseStatus.SHIPPED, purchase.getStatus());
    }

    @Test
    void readPurchase() {
        when(purchaseRepository.findPurchaseByOrderNumber(any(UUID.class))).thenReturn(Optional.of(purchase));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        ReadPurchaseResponse response = purchaseGuestService.readPurchase(purchase.getOrderNumber(), "password");

        assertNotNull(response);
        assertEquals(purchase.getId(), response.id());
    }

    @Test
    void deletePurchase() {
        when(purchaseRepository.findPurchaseByOrderNumber(any(UUID.class))).thenReturn(Optional.of(purchase));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        assertDoesNotThrow(() -> purchaseGuestService.deletePurchase(purchase.getOrderNumber(), "password"));
    }
    @Test
    void testValidateGuestThrowsPurchaseDoesNotExistException() {
        when(purchaseRepository.findPurchaseByOrderNumber(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(PurchaseDoesNotExistException.class, () -> purchaseGuestService.deletePurchase(purchase.getOrderNumber(), "password"));
    }

    @Test
    void testValidateGuestThrowsPurchaseNoAuthorizationException() {
        when(purchaseRepository.findPurchaseByOrderNumber(any(UUID.class))).thenReturn(Optional.of(purchase));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(PurchaseNoAuthorizationException.class, () -> purchaseGuestService.deletePurchase(purchase.getOrderNumber(), "password"));
    }

}