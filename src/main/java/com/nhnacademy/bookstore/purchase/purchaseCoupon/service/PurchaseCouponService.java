package com.nhnacademy.bookstore.purchase.purchaseCoupon.service;

import com.nhnacademy.bookstore.purchase.purchaseCoupon.dto.response.ReadPurchaseCouponDetailResponse;
import com.nhnacademy.bookstore.purchase.purchaseCoupon.dto.response.ReadPurchaseCouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PurchaseCouponService {
    Long create(Long purchaseId, Long couponFormId, int discountPrice);
    List<ReadPurchaseCouponResponse> read(Long purchaseId);

    Page<ReadPurchaseCouponDetailResponse> readByMemberId(Long memberId, Pageable pageable);
}
