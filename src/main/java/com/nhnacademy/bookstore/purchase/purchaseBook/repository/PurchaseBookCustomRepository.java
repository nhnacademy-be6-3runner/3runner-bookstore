package com.nhnacademy.bookstore.purchase.purchaseBook.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstore.purchase.purchaseBook.dto.response.ReadPurchaseBookResponse;

/**
 *
 */
public interface PurchaseBookCustomRepository {
	List<ReadPurchaseBookResponse> readBookPurchaseResponses(Long purchaseId);

	List<ReadPurchaseBookResponse> readGuestBookPurchaseResponses(String purchaseId);

	ReadPurchaseBookResponse readPurchaseBookResponse(Long purchaseBookId);

}
