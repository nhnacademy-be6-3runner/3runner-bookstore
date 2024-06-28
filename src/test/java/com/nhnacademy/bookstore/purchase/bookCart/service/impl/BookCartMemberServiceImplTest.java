package com.nhnacademy.bookstore.purchase.bookCart.service.impl;

import com.nhnacademy.bookstore.book.book.repository.BookRepository;
import com.nhnacademy.bookstore.entity.book.Book;
import com.nhnacademy.bookstore.entity.bookCart.BookCart;
import com.nhnacademy.bookstore.entity.cart.Cart;
import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.member.member.repository.MemberRepository;
import com.nhnacademy.bookstore.purchase.bookCart.dto.request.CreateBookCartRequest;
import com.nhnacademy.bookstore.purchase.bookCart.dto.request.DeleteBookCartRequest;
import com.nhnacademy.bookstore.purchase.bookCart.dto.request.ReadAllBookCartMemberRequest;
import com.nhnacademy.bookstore.purchase.bookCart.dto.request.UpdateBookCartRequest;
import com.nhnacademy.bookstore.purchase.bookCart.dto.response.ReadAllBookCartMemberResponse;
import com.nhnacademy.bookstore.purchase.bookCart.repository.BookCartRepository;
import com.nhnacademy.bookstore.purchase.cart.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
class BookCartMemberServiceImplTest {

	@InjectMocks
	private BookCartMemberServiceImpl bookCartMemberService;

	@Mock
	private BookCartRepository bookCartRepository;

	@Mock
	private CartRepository cartRepository;

	@Mock
	private BookRepository bookRepository;

	@Mock
	private MemberRepository memberRepository;

	private Member member;
	private Cart cart;
	private Book book;
	private BookCart bookCart;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		member = new Member();
		member.setId(1L);

		cart = new Cart();
		cart.setId(1L);
		cart.setMember(member);

		book = new Book();
		book.setId(1L);

		bookCart = new BookCart(2, book, cart);
		bookCart.setId(1L);


		when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
		when(cartRepository.findByMemberId(1L)).thenReturn(Optional.of(cart));
		when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
		when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
		when(bookCartRepository.findBookCartByBookIdAndCartId(1L, 1L)).thenReturn(Optional.of(bookCart));
		when(bookCartRepository.save(any(BookCart.class))).thenReturn(bookCart);
	}

	@Test
	void testReadAllCartMember() {
		ReadAllBookCartMemberRequest request = ReadAllBookCartMemberRequest.builder().userId(1L).build();
		when(bookCartRepository.findAllByCartId(1L)).thenReturn(Arrays.asList(bookCart));

		List<ReadAllBookCartMemberResponse> responses = bookCartMemberService.readAllCartMember(request);

		assertEquals(1, responses.size());
		assertEquals(2, responses.getFirst().quantity());
	}

	@Test
	void testCreateBookCartMember() {
		CreateBookCartRequest request = CreateBookCartRequest.builder()
			.userId(1L)
			.bookId(1L)
			.quantity(3)
			.build();

		Long bookCartId = bookCartMemberService.createBookCartMember(request);

		assertEquals(1L, bookCartId);
	}

	@Test
	void testUpdateBookCartMember() {
		UpdateBookCartRequest request = new UpdateBookCartRequest(1L, 1L, 5);

		Long bookCartId = bookCartMemberService.updateBookCartMember(request);

		assertEquals(1L, bookCartId);
	}

	@Test
	void testDeleteBookCartMember() {
		DeleteBookCartRequest request = new DeleteBookCartRequest(1L, 1L);

		when(bookCartRepository.findById(1L)).thenReturn(Optional.of(bookCart));
		doNothing().when(bookCartRepository).delete(bookCart);

		Long cartId = bookCartMemberService.deleteBookCartMember(request);

		verify(bookCartRepository, times(1)).delete(bookCart);
		assertEquals(1L, cartId);
	}
}
