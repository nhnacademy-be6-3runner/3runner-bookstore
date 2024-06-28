package com.nhnacademy.bookstore.book.bookLike.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstore.book.book.dto.response.BookListResponse;
import com.nhnacademy.bookstore.entity.bookLike.BookLike;

/**
 * 도서-좋아요 서비스 인터페이스입니다.
 *
 * @author 김은비
 */
public interface BookLikeService {
	BookLike findById(long bookLikeId);

	/**
	 * 도서-좋아요 추가 메서드입니다.
	 *
	 * @param memberId 회원
	 * @return bookLike id
	 */
	long createBookLike(long memberId, long bookId);

	/**
	 * 도서-좋아요 삭제 메서드입니다.
	 *
	 * @param bookLikeId bookLike id
	 */
	void deleteBookLike(long bookLikeId);

	/**
	 * 회원이 자신이 좋아요한 도서 목록을 조회하는 메서드입니다.
	 *
	 * @param memberId 회원
	 * @param pageable 페이지
	 * @return 도서 리스트
	 */
	Page<BookListResponse> findBookLikeByMemberId(long memberId, Pageable pageable);

	/**
	 * 도서의 좋아요 갯수를 반환하는 메서드입니다.
	 *
	 * @param bookId 도서 아이디
	 * @return 좋아요 갯수
	 */
	long countLikeByBookId(long bookId);

	/**
	 * 좋아요가 많은 순으로 조회하는 메서드입니다.
	 *
	 * @param pageable 페이지
	 * @return 도서 리스트
	 */
	Page<BookListResponse> findBooksOrderByLikes(Pageable pageable);
}
