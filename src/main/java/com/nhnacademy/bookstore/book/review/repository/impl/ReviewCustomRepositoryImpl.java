package com.nhnacademy.bookstore.book.review.repository.impl;

import com.nhnacademy.bookstore.book.review.dto.response.ReviewDetailResponse;
import com.nhnacademy.bookstore.book.review.dto.response.ReviewResponse;
import com.nhnacademy.bookstore.book.review.repository.ReviewCustomRepository;
import com.nhnacademy.bookstore.entity.member.QMember;
import com.nhnacademy.bookstore.entity.purchase.QPurchase;
import com.nhnacademy.bookstore.entity.purchaseBook.QPurchaseBook;
import com.nhnacademy.bookstore.entity.review.QReview;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QMember qMember = QMember.member;
    private final QPurchase qPurchase = QPurchase.purchase;
    private final QPurchaseBook qPurchaseBook = QPurchaseBook.purchaseBook;
    private final QReview qReview = QReview.review;

    public ReviewCustomRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public boolean existByPurchaseBook(long purchaseBookId, long userId) {
        Integer count = jpaQueryFactory
                .selectOne()
                .from(qPurchaseBook)
                .join(qPurchaseBook.purchase, qPurchase)
                .join(qPurchase.member, qMember)
                .where(qPurchaseBook.id.eq(purchaseBookId).and(qMember.id.eq(userId)))
                .fetchFirst();
        return count != null && count > 0;
    }

    @Override
    public ReviewDetailResponse getReviewDetail(long reviewId) {
//        List<ReviewDetailResponse> reviewDetailResponseList = jpaQueryFactory
//                .select(Projections.constructor(ReviewDetailResponse.class,
//                        qReview.id,
//                        qReview.title,
//                        qReview.content,
//                        qReview.rating,
//                        qMember.email))
//                .from(qReview)
//                .join();
        return null;
    }

    @Override
    public Page<ReviewResponse> getReviewsByBookId(long bookId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ReviewResponse> getReviewsByUserId(long userId, Pageable pageable) {
        return null;
    }
}
