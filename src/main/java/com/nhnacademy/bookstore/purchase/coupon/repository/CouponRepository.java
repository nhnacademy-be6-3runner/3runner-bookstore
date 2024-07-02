package com.nhnacademy.bookstore.purchase.coupon.repository;
import com.nhnacademy.bookstore.entity.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * jpa 쿠폰 저장소 인터페이스.
 *
 * @author 김병우
 */
public interface CouponRepository extends JpaRepository<Coupon, Long> {
}