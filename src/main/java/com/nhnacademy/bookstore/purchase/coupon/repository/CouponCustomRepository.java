package com.nhnacademy.bookstore.purchase.coupon.repository;


import com.nhnacademy.bookstore.purchase.coupon.dto.CouponResponse;

import java.util.List;

/**
 * QueryDsl 쿠폰 저장소 인터페이스.
 *
 * @author 김병우
 */
public interface CouponCustomRepository {
    List<CouponResponse> findMemberIdsByCouponFormIds(List<Long> couponFormIds);
}
