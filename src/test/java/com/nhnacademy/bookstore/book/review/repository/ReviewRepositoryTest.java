package com.nhnacademy.bookstore.book.review.repository;

import com.nhnacademy.bookstore.book.review.dto.response.ReviewAdminListResponse;
import com.nhnacademy.bookstore.book.review.dto.response.ReviewDetailResponse;
import com.nhnacademy.bookstore.book.review.dto.response.ReviewListResponse;
import com.nhnacademy.bookstore.book.review.repository.impl.ReviewCustomRepositoryImpl;
import com.nhnacademy.bookstore.entity.book.Book;
import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.entity.purchase.Purchase;
import com.nhnacademy.bookstore.entity.purchase.enums.MemberType;
import com.nhnacademy.bookstore.entity.purchase.enums.PurchaseStatus;
import com.nhnacademy.bookstore.entity.purchaseBook.PurchaseBook;
import com.nhnacademy.bookstore.entity.review.Review;
import com.nhnacademy.bookstore.member.member.dto.request.CreateMemberRequest;
import com.nhnacademy.bookstore.member.pointRecord.repository.PointRecordRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 리뷰 인터페이스 테스트입니다.
 *
 * @author 김은비
 */
@DataJpaTest
@Import(ReviewCustomRepositoryImpl.class)
class ReviewRepositoryTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PointRecordRepository pointRecordRepository;

    private PurchaseBook purchaseBook;
    private Member member;
    private Book book;
    private Review review;

    @BeforeEach
    public void setUp() {
        member = new Member(CreateMemberRequest.builder()
                .password("12345677")
                .name("1")
                .age(1)
                .phone("1")
                .birthday("2024-05-28")
                .email("dfdaf@nav.com")
                .build());
        entityManager.persist(member);

        Member member2 = new Member(CreateMemberRequest.builder()
                .password("12345677")
                .name("1")
                .age(1)
                .phone("1")
                .birthday("2024-05-28")
                .email("dfdaf2@nav.com")
                .build());
        entityManager.persist(member2);

        book = new Book(
                "책1",
                "책1입니다.",
                ZonedDateTime.now(),
                1000,
                10,
                900,
                0,
                true,
                "작가",
                "123456789",
                "출판사",
                null,
                null,
                null
        );
        entityManager.persist(book);

        Purchase purchase = new Purchase(
                UUID.randomUUID(),
                PurchaseStatus.CONFIRMATION,
                100,
                10,
                ZonedDateTime.now(),
                "road",
                "password",
                ZonedDateTime.now(),
                true,
                MemberType.MEMBER,
                member);
        entityManager.persist(purchase);

        Purchase purchase2 = new Purchase(
                UUID.randomUUID(),
                PurchaseStatus.CONFIRMATION,
                100,
                10,
                ZonedDateTime.now(),
                "road",
                "password",
                ZonedDateTime.now(),
                true,
                MemberType.MEMBER,
                member2);
        entityManager.persist(purchase2);

        purchaseBook = new PurchaseBook(book, 1, 100, purchase);
        entityManager.persist(purchaseBook);

        PurchaseBook purchaseBook2 = new PurchaseBook(book, 1, 100, purchase2);
        entityManager.persist(purchaseBook2);

        review = new Review(
                purchaseBook,
                "리뷰입니다.",
                "아주 추천합니다. 좋은 책입니다.",
                4.5,
                null,
                null,
                null
        );
        entityManager.persist(review);
        Review review2 = new Review(
                purchaseBook2,
                "리뷰입니다.",
                "아주 추천합니다. 좋은 책입니다.",
                4.0,
                null,
                null,
                null
        );
        entityManager.persist(review2);

        entityManager.flush();
    }

    @DisplayName("회원이 주문한 도서가 존재 유무 테스트")
    @Test
    void testExistByPurchaseBook() {
        boolean exists = reviewRepository.existByPurchaseBook(purchaseBook.getId(), member.getId());
        assertThat(exists).isTrue();
    }

    @DisplayName("리뷰 상세보기 테스트")
    @Test
    void testGetReviewDetail() {
        ReviewDetailResponse reviewDetailResponse = reviewRepository.getReviewDetail(review.getId());

        assertThat(reviewDetailResponse).isNotNull();
        assertThat(reviewDetailResponse.bookId()).isEqualTo(book.getId());
        assertThat(reviewDetailResponse.bookTitle()).isEqualTo(book.getTitle());
        assertThat(reviewDetailResponse.reviewId()).isEqualTo(review.getId());
        assertThat(reviewDetailResponse.reviewTitle()).isEqualTo(review.getTitle());
        assertThat(reviewDetailResponse.reviewContent()).isEqualTo(review.getContent());
        assertThat(reviewDetailResponse.ratings()).isEqualTo(review.getRating());
        assertThat(reviewDetailResponse.memberEmail()).isEqualTo(member.getEmail());
        assertThat(reviewDetailResponse.updated()).isEqualTo(review.isUpdated());
    }

    @Test
    @DisplayName("리뷰 목록 조회 테스트")
    void testGetReviewList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ReviewAdminListResponse> reviewPage = reviewRepository.getReviewList(pageable);

        assertThat(reviewPage).isNotNull();
        assertThat(reviewPage.getContent()).isNotEmpty();
        assertThat(reviewPage.getTotalElements()).isPositive();
        assertThat(reviewPage.getContent().getFirst().reviewId()).isEqualTo(review.getId());
        assertThat(reviewPage.getContent().getFirst().title()).isEqualTo(review.getTitle());
        assertThat(reviewPage.getContent().getFirst().imgUrl()).isNull();
        assertThat(reviewPage.getContent().getFirst().rating()).isEqualTo(review.getRating());
        assertThat(reviewPage.getContent().getFirst().memberEmail()).isEqualTo(member.getEmail());
    }

    @DisplayName("책 아이디로 리뷰 조회 테스트")
    @Test
    void testGetReviewsByBookId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ReviewListResponse> reviewPage = reviewRepository.getReviewsByBookId(book.getId(), pageable);

        assertThat(reviewPage).isNotNull();
        assertThat(reviewPage.getContent()).isNotEmpty();
        assertThat(reviewPage.getTotalElements()).isPositive();
        assertThat(reviewPage.getContent().getFirst().reviewId()).isEqualTo(review.getId());
        assertThat(reviewPage.getContent().getFirst().title()).isEqualTo(review.getTitle());
        assertThat(reviewPage.getContent().getFirst().imgUrl()).isNull();
        assertThat(reviewPage.getContent().getFirst().rating()).isEqualTo(review.getRating());
        assertThat(reviewPage.getContent().getFirst().memberEmail()).isEqualTo(member.getEmail());
    }

    @DisplayName("사용자 아이디로 리뷰 조회 테스트")
    @Test
    void testGetReviewsByUserId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ReviewListResponse> reviewPage = reviewRepository.getReviewsByUserId(member.getId(), pageable);

        assertThat(reviewPage).isNotNull();
        assertThat(reviewPage.getContent()).isNotEmpty();
        assertThat(reviewPage.getTotalElements()).isPositive();
        assertThat(reviewPage.getContent().getFirst().reviewId()).isEqualTo(review.getId());
        assertThat(reviewPage.getContent().getFirst().title()).isEqualTo(review.getTitle());
        assertThat(reviewPage.getContent().getFirst().imgUrl()).isNull();
        assertThat(reviewPage.getContent().getFirst().rating()).isEqualTo(review.getRating());
        assertThat(reviewPage.getContent().getFirst().memberEmail()).isEqualTo(member.getEmail());
    }
}
