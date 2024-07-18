package com.nhnacademy.bookstore.purchase.coupon.repository.impl;

import com.nhnacademy.bookstore.entity.coupon.QCoupon;
import com.nhnacademy.bookstore.entity.member.QMember;
import com.nhnacademy.bookstore.purchase.coupon.dto.CouponResponse;
import com.nhnacademy.bookstore.purchase.coupon.repository.CouponCustomRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * queryDsl 쿠폰 저장소 구현체.
 *
 * @author 김병우
 */
@Slf4j
@Repository
public class CouponCustomRepositoryImpl implements CouponCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final QMember qMember= QMember.member;
    private final QCoupon qCoupon = QCoupon.coupon;

    public CouponCustomRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 쿠폰폼아이디에 해당하는 맴버아이디 읽기.
     *
     * @param couponFormIds 쿠폰폼아이디 리스트
     * @return 쿠폰 dto 리스트
     */
    @Override
    public List<CouponResponse> findMemberIdsByCouponFormIds(List<Long> couponFormIds) {
        List<Tuple> tuples = queryFactory
                .select(qMember.id,
                        qCoupon.id
                )
                .from(qCoupon)
                .join(qCoupon.member, qMember)
                .where(qCoupon.couponFormId.in(couponFormIds))
                .fetch();

        return tuples.stream()
                .map(tuple -> new CouponResponse(
                        tuple.get(qMember.id),
                        tuple.get(qCoupon.id)
                )).toList();
    }
}