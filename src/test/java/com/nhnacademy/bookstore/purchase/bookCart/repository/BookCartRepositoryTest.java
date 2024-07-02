package com.nhnacademy.bookstore.purchase.bookCart.repository;

import com.nhnacademy.bookstore.entity.book.Book;
import com.nhnacademy.bookstore.entity.bookCart.BookCart;
import com.nhnacademy.bookstore.entity.cart.Cart;
import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.member.member.dto.request.CreateMemberRequest;
import com.nhnacademy.bookstore.purchase.bookCart.repository.BookCartRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class BookCartRepositoryTest {

	@Autowired
	private BookCartRepository bookCartRepository;


	@PersistenceContext
	private EntityManager entityManager;

	private Member member;
	private Cart cart;
	private Book book;
	private BookCart bookCart;

	@BeforeEach
	void setUp() {
		// 테스트에 필요한 객체들 생성
		CreateMemberRequest request = CreateMemberRequest.builder()
			.password("password")
			.name("John Doe")
			.age(30)
			.phone("1234567890")
			.email("john.doe@example.com")
			.birthday(ZonedDateTime.now()).build();

		member = new Member(request);

		cart = new Cart(member);
		book = new Book(
			"Sample Book Title",
			"This is a sample description",
			ZonedDateTime.now(),
			1000,
			10,
			800,
			1,
			true,
			"Author Name",
			"1234567890123",
			"Publisher Name",
			null,
			null,
			null
		);
		bookCart = new BookCart(2, book, cart);

		// 객체 저장
		entityManager.persist(member);
		entityManager.persist(cart);
		entityManager.persist(book);
		entityManager.persist(bookCart);
	}

	@Test
	public void testFindAllByCartId() {
		// Given

		// When
		List<BookCart> result = bookCartRepository.findAllByCartId(cart.getId());

		// Then
		assertFalse(result.isEmpty());
		assertEquals(1, result.size());
		assertEquals(bookCart.getId(), result.getFirst().getId());
		assertEquals(book.getId(), result.getFirst().getBook().getId());
		assertEquals(cart.getId(), result.getFirst().getCart().getId());
	}

	@Test
	public void testDeleteByCart() {
		// When
		bookCartRepository.deleteByCart(cart);

		// Then
		Optional<BookCart> deletedBookCart = bookCartRepository.findById(bookCart.getId());
		assertTrue(deletedBookCart.isEmpty());
	}
}
