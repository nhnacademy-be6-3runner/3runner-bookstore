package com.nhnacademy.bookstore.book.bookLike.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstore.book.book.dto.response.BookListResponse;
import com.nhnacademy.bookstore.book.book.repository.BookRepository;
import com.nhnacademy.bookstore.book.bookLike.exception.BookLikeNotExistsException;
import com.nhnacademy.bookstore.book.bookLike.repository.BookLikeRepository;
import com.nhnacademy.bookstore.book.bookLike.service.BookLikeService;
import com.nhnacademy.bookstore.entity.book.Book;
import com.nhnacademy.bookstore.entity.bookLike.BookLike;
import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.member.member.exception.MemberNotExistsException;
import com.nhnacademy.bookstore.member.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookLikeServiceImpl implements BookLikeService {

	private final BookLikeRepository bookLikeRepository;
	private final MemberRepository memberRepository;
	private final BookRepository bookRepository;

	@Override
	public BookLike findById(long bookLikeId) {
		Optional<BookLike> bookLike = bookLikeRepository.findById(bookLikeId);
		if (bookLike.isEmpty()) {
			throw new BookLikeNotExistsException("존재하지 않는 도서-좋아요입니다.");
		}
		return bookLike.get();
	}

	@Override
	public long createBookLike(long memberId, long bookId) {
		Member member = memberRepository.findById(memberId).orElseThrow(MemberNotExistsException::new);
		Book book = bookRepository.findById(bookId).orElseThrow();

		BookLike bookLike = new BookLike();
		bookLike.setMember(member);
		bookLike.setBook(book);

		bookLikeRepository.save(bookLike);
		return bookLike.getId();
	}

	@Override
	public void deleteBookLike(long bookLikeId) {
		if (!bookLikeRepository.existsById(bookLikeId)) {
			throw new BookLikeNotExistsException("존재하지 않는 도서-좋아요입니다.");
		}
		bookLikeRepository.deleteById(bookLikeId);
	}

	@Override
	public Page<BookListResponse> findBookLikeByMemberId(long memberId, Pageable pageable) {
		return bookLikeRepository.findBookLikeByMemberId(memberId, pageable);
	}

	@Override
	public long countLikeByBookId(long bookId) {
		return bookLikeRepository.countLikeByBookId(bookId);
	}

	@Override
	public Page<BookListResponse> findBooksOrderByLikes(Pageable pageable) {
		return bookLikeRepository.findBooksOrderByLikes(pageable);
	}
}
