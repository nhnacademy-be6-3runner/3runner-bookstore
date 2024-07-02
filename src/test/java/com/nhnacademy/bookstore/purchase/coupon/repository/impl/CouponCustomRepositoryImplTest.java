package com.nhnacademy.bookstore.purchase.coupon.repository.impl;

import com.nhnacademy.bookstore.entity.coupon.Coupon;
import com.nhnacademy.bookstore.entity.coupon.QCoupon;
import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.entity.member.QMember;
import com.nhnacademy.bookstore.purchase.coupon.dto.CouponResponse;
import com.nhnacademy.bookstore.purchase.coupon.repository.CouponCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(CouponCustomRepositoryImpl.class)
class CouponCustomRepositoryImplTest {

    @Autowired
    private CouponCustomRepository couponCustomRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Member member1;
    private Member member2;
    private Coupon coupon1;
    private Coupon coupon2;

    @BeforeEach
    void setUp() {
        member1 = new Member();;
        member1.setName("John Doe");

        member2 = new Member();
        member2.setName("Jane Doe");

        entityManager.persist(member1);
        entityManager.persist(member2);

        coupon1 = new Coupon();
        coupon1.setId(1L);
        coupon1.setMember(member1);
        coupon1.setCouponFormId(1L);

        coupon2 = new Coupon();
        coupon2.setId(2L);
        coupon2.setMember(member2);
        coupon2.setCouponFormId(2L);

        entityManager.persist(coupon1);
        entityManager.persist(coupon2);

        entityManager.flush();
        entityManager.clear();
    }

}