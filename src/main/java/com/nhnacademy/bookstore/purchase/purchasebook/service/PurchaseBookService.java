package com.nhnacademy.bookstore.purchase.purchasebook.service;

import java.util.List;

import com.nhnacademy.bookstore.purchase.purchasebook.dto.request.CreatePurchaseBookRequest;
import com.nhnacademy.bookstore.purchase.purchasebook.dto.request.UpdatePurchaseBookRequest;
import com.nhnacademy.bookstore.purchase.purchasebook.dto.response.ReadPurchaseBookResponse;

/**
 * 주문-책 interface
 *
 * @author 정주혁
 */
public interface PurchaseBookService {
    void deletePurchaseBook(long purchaseBookId);

    Long createPurchaseBook(CreatePurchaseBookRequest createPurchaseBookRequest);

    Long updatePurchaseBook(UpdatePurchaseBookRequest updatePurchaseBookRequest);

    List<ReadPurchaseBookResponse> readBookByPurchaseResponses(Long purchaseId, Long memberId);

    List<ReadPurchaseBookResponse> readGuestBookByPurchaseResponses(String purchaseId);

}
