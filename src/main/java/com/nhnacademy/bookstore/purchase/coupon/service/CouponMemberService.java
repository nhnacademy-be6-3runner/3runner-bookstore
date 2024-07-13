package com.nhnacademy.bookstore.purchase.coupon.service;

import com.nhnacademy.bookstore.entity.coupon.Coupon;
import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.purchase.coupon.dto.ReadCouponResponseForMember;
import com.nhnacademy.bookstore.purchase.coupon.feign.dto.request.CreateCouponFormRequest;
import com.nhnacademy.bookstore.purchase.coupon.feign.dto.response.ReadCouponFormResponse;

import java.util.List;

public interface CouponMemberService {
    List<ReadCouponFormResponse> readMemberCoupons(Long memberId);

    Long useCoupons(Long couponFormId, Long memberId);

    void issueBirthdayCoupon(Long memberId);

    void issueWelcomeCoupon(Member member);

    Long refundCoupons(Long couponFormId, Long memberId);

    Long readCoupon(Long couponFormId);

    Long registorCoupon(String code, Long memberId);


    Boolean registorCouponForBook(Long bookId, Long memberId);
}
