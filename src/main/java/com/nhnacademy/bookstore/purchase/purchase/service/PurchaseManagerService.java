package com.nhnacademy.bookstore.purchase.purchase.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstore.entity.purchase.enums.PurchaseStatus;
import com.nhnacademy.bookstore.purchase.purchase.dto.response.ReadPurchaseResponse;

public interface PurchaseManagerService {

	Page<ReadPurchaseResponse> readPurchaseAll(Pageable pageable);

	Long updatePurchaseStatus(String purchaseId, String status);
}
