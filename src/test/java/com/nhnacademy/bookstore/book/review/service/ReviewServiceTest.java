package com.nhnacademy.bookstore.book.review.service;

import com.nhnacademy.bookstore.book.book.repository.BookRepository;
import com.nhnacademy.bookstore.book.review.dto.request.CreateReviewRequest;
import com.nhnacademy.bookstore.book.review.dto.request.DeleteReviewRequest;
import com.nhnacademy.bookstore.book.review.dto.response.ReviewDetailResponse;
import com.nhnacademy.bookstore.book.review.dto.response.ReviewListResponse;
import com.nhnacademy.bookstore.book.review.repository.ReviewRepository;
import com.nhnacademy.bookstore.book.review.service.impl.ReviewServiceImpl;
import com.nhnacademy.bookstore.entity.book.Book;
import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.entity.purchase.Purchase;
import com.nhnacademy.bookstore.entity.purchase.enums.MemberType;
import com.nhnacademy.bookstore.entity.purchase.enums.PurchaseStatus;
import com.nhnacademy.bookstore.entity.purchaseBook.PurchaseBook;
import com.nhnacademy.bookstore.entity.review.Review;
import com.nhnacademy.bookstore.member.member.dto.request.CreateMemberRequest;
import com.nhnacademy.bookstore.member.member.repository.MemberRepository;
import com.nhnacademy.bookstore.member.member.service.MemberPointService;
import com.nhnacademy.bookstore.member.pointRecord.repository.PointRecordRepository;
import com.nhnacademy.bookstore.purchase.purchaseBook.repository.PurchaseBookRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PurchaseBookRepository purchaseBookRepository;

    @Mock
    private PointRecordRepository pointRecordRepository;

    @Mock
    private MemberPointService memberPointService;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private CreateReviewRequest createReviewRequest;
    private CreateReviewRequest updateReviewRequest;
    private DeleteReviewRequest deleteReviewRequest;
    private Review review;
    private PurchaseBook purchaseBook;
    private Member member1;
    private Member member2;

    @BeforeEach
    public void setUp() {
        createReviewRequest = new CreateReviewRequest("좋은 책입니다", "추천합니다", 5, null);
        updateReviewRequest = new CreateReviewRequest("좋은 책입니다2", "추천합니다", 5, null);
        deleteReviewRequest = new DeleteReviewRequest("부적절한 내용");

        member1 = new Member(CreateMemberRequest.builder()
                .password("12345677")
                .name("1")
                .age(1)
                .phone("1")
                .birthday("2024-05-28")
                .email("dfdaf@nav.com")
                .build());
        member1.setId(1L);

        member2 = new Member(CreateMemberRequest.builder()
                .password("12345677")
                .name("1")
                .age(1)
                .phone("1")
                .birthday("2024-05-28".toString())
                .email("dfdaf2@nav.com")
                .build());
        member2.setId(2L);

        Book book = new Book(
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
                member1);

        purchaseBook = new PurchaseBook(book, 1, 100, purchase);

        review = new Review(
                purchaseBook,
                "리뷰입니다.",
                "아주 추천합니다. 좋은 책입니다.",
                4.5,
                null,
                null,
                null
        );
    }

    @DisplayName("리뷰 생성 테스트")
    @Test
    void testCreateReview() {
        given(purchaseBookRepository.findById(1L)).willReturn(Optional.of(purchaseBook));
        given(reviewRepository.existByPurchaseBook(1L, 1L)).willReturn(true);
        given(memberPointService.updatePoint(anyLong(), anyLong())).willReturn(1L);

        Long reviewId = reviewService.createReview(1L, 1L, createReviewRequest);

        assertThat(reviewId).isNotNull();

        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(reviewCaptor.capture());
        Review savedReview = reviewCaptor.getValue();

        assertThat(savedReview).isNotNull();
        assertThat(savedReview.getTitle()).isEqualTo(createReviewRequest.title());
        assertThat(savedReview.getContent()).isEqualTo(createReviewRequest.content());
        assertThat(savedReview.getRating()).isEqualTo(createReviewRequest.ratings());
    }

    @DisplayName("리뷰 수정 테스트")
    @Test
    void testUpdateReview() {
        given(reviewRepository.findById(1L)).willReturn(Optional.of(review));

        Long reviewId = reviewService.updateReview(1L, member1.getId(), updateReviewRequest);

        assertThat(reviewId).isEqualTo(review.getId());
        assertThat(review.getTitle()).isEqualTo(updateReviewRequest.title());
        assertThat(review.getContent()).isEqualTo(updateReviewRequest.content());
        assertThat(review.getRating()).isEqualTo(updateReviewRequest.ratings());

        verify(reviewRepository, Mockito.never()).save(review);
    }

    @DisplayName("리뷰 상세 조회 테스트")
    @Test
    void testReadDetailReview() {
        ReviewDetailResponse expectedResponse = ReviewDetailResponse.builder()
                .bookId(1L)
                .bookTitle("책 제목")
                .reviewId(1L)
                .reviewTitle("리뷰 제목")
                .reviewContent("리뷰 내용")
                .ratings(5.0)
                .memberEmail("member@example.com")
                .createdAt(ZonedDateTime.now())
                .updated(false)
                .updatedAt(null)
                .build();

        given(reviewRepository.existsById(1L)).willReturn(true);
        given(reviewRepository.getReviewDetail(1L)).willReturn(expectedResponse);

        ReviewDetailResponse response = reviewService.readDetailReview(1L);

        assertThat(response).isEqualTo(expectedResponse);
        verify(reviewRepository).getReviewDetail(1L);
    }

    @DisplayName("책 아이디로 리뷰 조회 테스트")
    @Test
    void testReadAllReviewsByBookId() {
        Pageable pageable = PageRequest.of(0, 10);
        ReviewListResponse reviewListResponse = ReviewListResponse.builder()
                .reviewId(1L)
                .title("리뷰 제목")
                .imgUrl("이미지 URL")
                .rating(5.0)
                .memberEmail("member@example.com")
                .createdAt(ZonedDateTime.now())
                .build();

        Page<ReviewListResponse> expectedPage = new PageImpl<>(Arrays.asList(reviewListResponse));
        given(bookRepository.existsById(1L)).willReturn(true);
        given(reviewRepository.getReviewsByBookId(1L, pageable)).willReturn(expectedPage);

        Page<ReviewListResponse> actualPage = reviewService.readAllReviewsByBookId(1L, pageable);

        assertThat(actualPage).isEqualTo(expectedPage);
        verify(reviewRepository).getReviewsByBookId(1L, pageable);
    }

    @DisplayName("멤버 아이디로 리뷰 조회 테스트")
    @Test
    void testReadAllReviewsByMemberId() {
        Pageable pageable = PageRequest.of(0, 10);
        ReviewListResponse reviewListResponse = ReviewListResponse.builder()
                .reviewId(1L)
                .title("리뷰 제목")
                .imgUrl("이미지 URL")
                .rating(5.0)
                .memberEmail("member@example.com")
                .createdAt(ZonedDateTime.now())
                .build();

        Page<ReviewListResponse> expectedPage = new PageImpl<>(Arrays.asList(reviewListResponse));
        given(memberRepository.existsById(1L)).willReturn(true);
        given(reviewRepository.getReviewsByUserId(1L, pageable)).willReturn(expectedPage);

        Page<ReviewListResponse> actualPage = reviewService.readAllReviewsByMemberId(1L, pageable);

        assertThat(actualPage).isEqualTo(expectedPage);
        verify(reviewRepository).getReviewsByUserId(1L, pageable);
    }

}
