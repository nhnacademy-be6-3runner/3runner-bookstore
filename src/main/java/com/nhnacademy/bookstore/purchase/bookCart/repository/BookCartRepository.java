package com.nhnacademy.bookstore.purchase.bookCart.repository;

import com.nhnacademy.bookstore.entity.book.Book;
import com.nhnacademy.bookstore.entity.bookCart.BookCart;
import com.nhnacademy.bookstore.entity.cart.Cart;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.nhnacademy.bookstore.entity.cart.Cart;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 도서장바구니 JPA 저장소.
 *
 * @author 김병우
 */
public interface BookCartRepository extends JpaRepository<BookCart, Long> {

	@Query("select bk from BookCart bk where bk.cart.id = :cartId")
	List<BookCart> findAllByCartId(Long cartId);

	void deleteByCart(Cart cart);

	Optional<BookCart> findBookCartByBookIdAndCartId(Long bookId, Long cartId);

    Optional<BookCart> findByBookAndCart(Book book, Cart cart);

	boolean existsBookCartByBookAndCart(Book book, Cart cart);
}
