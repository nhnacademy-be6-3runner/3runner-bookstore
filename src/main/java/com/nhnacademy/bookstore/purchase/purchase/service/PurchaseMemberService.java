package com.nhnacademy.bookstore.purchase.purchase.service;

import com.nhnacademy.bookstore.purchase.purchase.dto.request.CreatePurchaseRequest;
import com.nhnacademy.bookstore.purchase.purchase.dto.request.UpdatePurchaseRequest;
import com.nhnacademy.bookstore.purchase.purchase.dto.response.ReadPurchaseResponse;

public interface PurchaseMemberService {
    Long createPurchase(CreatePurchaseRequest createPurchaseRequest, Long memberId);
    Long updatePurchase(UpdatePurchaseRequest updatePurchaseRequest, Long memberId, Long purchaseId);
    ReadPurchaseResponse readPurchase(Long memberId, Long purchaseId);
    void deletePurchase(Long memberId, Long purchaseId);
}