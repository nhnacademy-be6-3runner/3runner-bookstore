package com.nhnacademy.bookstore.book.review.repository;

import com.nhnacademy.bookstore.book.review.dto.response.ReviewDetailResponse;
import com.nhnacademy.bookstore.book.review.dto.response.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewCustomRepository {
    // TODO 리뷰 등록할 수 있는지 확인 -> 주문-도서 아이디에서 주문 아이디 확인 -> 주문 아이디에서 유저 아이디 확인 -> 등록하려는 유저와 주문 아이디의 유저가 일치하는지 확인
    // 주문서 쪽에서 리뷰를 등록하니까 주문-도서 id 값을 넘겨옴
    boolean existByPurchaseBook(long purchaseBookId, long userId);

    // TODO 리뷰 조회 -> 상세 조회
    ReviewDetailResponse getReviewDetail(long reviewId);

    // TODO 책 아이디로 리뷰 전체 조회
    Page<ReviewResponse> getReviewsByBookId(long bookId, Pageable pageable);

    // TODO 사용자 아이디로 리뷰 전체 조회
    Page<ReviewResponse> getReviewsByUserId(long userId, Pageable pageable);
}
