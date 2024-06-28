package com.nhnacademy.bookstore.purchase.purchase.service;

import com.nhnacademy.bookstore.purchase.purchase.dto.request.CreatePurchaseRequest;
import com.nhnacademy.bookstore.purchase.purchase.dto.request.UpdatePurchaseGuestRequest;
import com.nhnacademy.bookstore.purchase.purchase.dto.response.ReadPurchaseResponse;

import java.util.UUID;

public interface PurchaseGuestService {
    Long createPurchase(CreatePurchaseRequest createPurchaseRequest);
    Long updatePurchase(UpdatePurchaseGuestRequest updatePurchaseGuestRequest);
    ReadPurchaseResponse readPurchase( UUID orderNumber, String password);
    void deletePurchase(UUID orderNumber, String password);
}
