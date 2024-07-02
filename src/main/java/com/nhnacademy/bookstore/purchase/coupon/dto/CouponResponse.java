package com.nhnacademy.bookstore.purchase.coupon.dto;

import lombok.Builder;

@Builder
public record CouponResponse(Long memberId, long couponId) {
}
