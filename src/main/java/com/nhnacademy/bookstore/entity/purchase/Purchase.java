package com.nhnacademy.bookstore.entity.purchase;

import com.nhnacademy.bookstore.entity.pointRecord.PointRecord;
import com.nhnacademy.bookstore.entity.purchase.enums.MemberType;
import com.nhnacademy.bookstore.entity.purchase.enums.PurchaseStatus;
import com.nhnacademy.bookstore.entity.purchaseBook.PurchaseBook;
import com.nhnacademy.bookstore.entity.purchaseCoupon.PurchaseCoupon;
import com.nhnacademy.bookstore.entity.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter@Setter
@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(unique = true)
    private UUID orderNumber;

    @NotNull
    private PurchaseStatus status;

    @NotNull
    private int deliveryPrice;

    @NotNull
    private int totalPrice;

    @NotNull
    private ZonedDateTime createdAt;


    @Lob
    @Column(columnDefinition = "TEXT")
    @NotNull
    private String road;

    private String password;

    @NotNull
    private MemberType memberType;

    @ManyToOne
    private Member member;

    @OneToOne
    PointRecord pointRecord;


    //연결
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseBook> purchaseBookList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseCoupon> purchaseCouponList = new ArrayList<>();

    public Purchase(UUID orderNumber, PurchaseStatus status, int deliveryPrice, int totalPrice, ZonedDateTime createdAt, String road, String password, MemberType memberType, Member member, PointRecord pointRecord, List<PurchaseBook> purchaseBookList, List<PurchaseCoupon> purchaseCouponList) {
        this.orderNumber = orderNumber;
        this.status = status;
        this.deliveryPrice = deliveryPrice;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.road = road;
        this.password = password;
        this.memberType = memberType;
        this.member = member;
        this.pointRecord = pointRecord;
        this.purchaseBookList = purchaseBookList;
        this.purchaseCouponList = purchaseCouponList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Purchase purchase)) return false;
        return Objects.equals(getOrderNumber(), purchase.getOrderNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getOrderNumber());
    }

}
