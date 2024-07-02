package com.nhnacademy.bookstore.entity.coupon;

import com.nhnacademy.bookstore.entity.coupon.enums.CouponStatus;
import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.entity.purchaseBook.PurchaseBook;
import com.nhnacademy.bookstore.entity.purchaseCoupon.PurchaseCoupon;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity@Getter@Setter
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long couponFormId;


    @NotNull
    private CouponStatus couponStatus;

    @NotNull
    private ZonedDateTime issuedAt;

    @ManyToOne
    private Member member;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseCoupon> purchaseCouponList = new ArrayList<>();


}
