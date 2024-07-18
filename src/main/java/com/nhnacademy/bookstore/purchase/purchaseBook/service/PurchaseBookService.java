package com.nhnacademy.bookstore.purchase.purchaseBook.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstore.purchase.purchaseBook.dto.request.CreatePurchaseBookRequest;
import com.nhnacademy.bookstore.purchase.purchaseBook.dto.request.UpdatePurchaseBookRequest;
import com.nhnacademy.bookstore.purchase.purchaseBook.dto.response.ReadPurchaseBookResponse;

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
