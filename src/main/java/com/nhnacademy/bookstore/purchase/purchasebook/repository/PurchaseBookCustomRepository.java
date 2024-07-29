package com.nhnacademy.bookstore.purchase.purchasebook.repository;

import java.util.List;

import com.nhnacademy.bookstore.purchase.purchasebook.dto.response.ReadPurchaseBookResponse;

/**
 *
 */
public interface PurchaseBookCustomRepository {
	List<ReadPurchaseBookResponse> readBookPurchaseResponses(Long purchaseId);

	List<ReadPurchaseBookResponse> readGuestBookPurchaseResponses(String purchaseId);

	ReadPurchaseBookResponse readPurchaseBookResponse(Long purchaseBookId);

}
