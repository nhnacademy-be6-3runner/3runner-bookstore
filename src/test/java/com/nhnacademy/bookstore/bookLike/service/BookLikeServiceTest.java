package com.nhnacademy.bookstore.bookLike.service;

import com.nhnacademy.bookstore.book.book.dto.response.BookListResponse;
import com.nhnacademy.bookstore.book.book.repository.BookRepository;
import com.nhnacademy.bookstore.book.bookLike.exception.BookLikeNotExistsException;
import com.nhnacademy.bookstore.book.bookLike.repository.BookLikeRepository;
import com.nhnacademy.bookstore.book.bookLike.service.impl.BookLikeServiceImpl;
import com.nhnacademy.bookstore.entity.book.Book;
import com.nhnacademy.bookstore.entity.bookLike.BookLike;
import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.member.member.dto.request.CreateMemberRequest;
import com.nhnacademy.bookstore.member.member.exception.MemberNotExistsException;
import com.nhnacademy.bookstore.member.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * 도서 좋아요 기능 서비스 테스트입니다.
 *
 * @author 김은비
 */
@ExtendWith(MockitoExtension.class)
public class BookLikeServiceTest {

    @Mock
    private BookLikeRepository bookLikeRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookLikeServiceImpl bookLikeService;

    private Member member1;
    private Member member2;
    private Book book1;
    private Book book2;
    private BookLike bookLike1;
    private BookLike bookLike2;
    private BookLike bookLike3;

    @BeforeEach
    public void setUp() {
        member1 = new Member(CreateMemberRequest.builder().password("1").name("1").age(1).phone("1").birthday(ZonedDateTime.now()).email("dfdaf@nav.com").build());

        member2 = new Member(CreateMemberRequest.builder().password("1").name("1").age(1).phone("1").birthday(ZonedDateTime.now()).email("dfdaf2@nav.com").build());

        book1 = new Book("책1", "책1입니다.", ZonedDateTime.now(), 1000, 10, 900, 0, true, "작가", "123456789", "출판사", null, null, null);

        book2 = new Book("책2", "책2입니다.", ZonedDateTime.now(), 1000, 10, 900, 0, true, "작가r", "123456789", "출판사", null, null, null);

        bookLike1 = new BookLike(1L, null, book1, member1);
        bookLike2 = new BookLike(2L, null, book2, member2);
        bookLike3 = new BookLike(3L, null, book1, member2);
    }

    @DisplayName("좋아요 아이디 찾기")
    @Test
    public void testFindById_existById() {
        given(bookLikeRepository.findById(1L)).willReturn(Optional.of(bookLike1));

        BookLike foundBookLike = bookLikeService.findById(1L);

        assertThat(foundBookLike).isNotNull();
        assertThat(foundBookLike.getId()).isEqualTo(1L);
        verify(bookLikeRepository).findById(1L);
    }

    @DisplayName("존재하지 않는 좋아요 아이디 찾기")
    @Test
    public void testFindById_notExistById() {
        given(bookLikeRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(BookLikeNotExistsException.class, () -> bookLikeService.findById(1L));
    }

    @DisplayName("좋아요 생성 성공")
    @Test
    public void testCreateBookLike() {
        given(memberRepository.findById(1L)).willReturn(Optional.of(member1));
        given(bookRepository.findById(1L)).willReturn(Optional.of(book1));

        long bookLikeId = bookLikeService.createBookLike(1L, 1L);

        assertTrue(bookLikeId == 0L);
    }

    @DisplayName("존재하지 않는 멤버로 좋아요 추가")
    @Test
    public void testCreateBookLike_notExistByMember() {
        given(memberRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(MemberNotExistsException.class, () -> bookLikeService.createBookLike(1L, 1L));
    }

    @DisplayName("좋아요 삭제 성공")
    @Test
    public void testDeleteBookLike_existById() {
        given(bookLikeRepository.existsById(1L)).willReturn(true);

        bookLikeService.deleteBookLike(1L);

        verify(bookLikeRepository).deleteById(1L);
    }

    @DisplayName("존재하지 않는 좋아요 삭제")
    @Test
    public void testDeleteBookLike_notExistById() {
        given(bookLikeRepository.existsById(1L)).willReturn(false);

        assertThrows(BookLikeNotExistsException.class, () -> bookLikeService.deleteBookLike(1L));
    }

    @Test
    public void testFindBookLikeByMemberId() {
        Pageable pageable = PageRequest.of(0, 10);
        BookListResponse bookListResponse1 = new BookListResponse(1, "책1", 1000, 900, "작가", "url1");
        BookListResponse bookListResponse2 = new BookListResponse(2, "책2", 1000, 900, "작가r", "url2");
        Page<BookListResponse> expectedPage = new PageImpl<>(Arrays.asList(bookListResponse1, bookListResponse2));
        given(bookLikeRepository.findBookLikeByMemberId(1L, pageable)).willReturn(expectedPage);

        Page<BookListResponse> actualPage = bookLikeService.findBookLikeByMemberId(1L, pageable);

        assertThat(actualPage).isEqualTo(expectedPage);
        verify(bookLikeRepository).findBookLikeByMemberId(1L, pageable);
    }

    @Test
    public void testCountLikeByBookId() {
        given(bookLikeRepository.countLikeByBookId(1L)).willReturn(10L);

        long count = bookLikeService.countLikeByBookId(1L);

        assertThat(count).isEqualTo(10L);
        verify(bookLikeRepository).countLikeByBookId(1L);
    }

    @Test
    public void testFindBooksOrderByLikes() {
        Pageable pageable = PageRequest.of(0, 10);
        BookListResponse bookListResponse1 = new BookListResponse(1, "책1", 1000, 900, "작가", "url1");
        BookListResponse bookListResponse2 = new BookListResponse(2, "책2", 1000, 900, "작가r", "url2");
        Page<BookListResponse> expectedPage = new PageImpl<>(Arrays.asList(bookListResponse1, bookListResponse2));
        given(bookLikeRepository.findBooksOrderByLikes(pageable)).willReturn(expectedPage);

        Page<BookListResponse> actualPage = bookLikeService.findBooksOrderByLikes(pageable);

        assertThat(actualPage).isEqualTo(expectedPage);
        verify(bookLikeRepository).findBooksOrderByLikes(pageable);
    }
}
