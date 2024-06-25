package com.nhnacademy.bookstore.purchase.bookCart.repository;

import com.nhnacademy.bookstore.entity.bookCart.BookCart;
import com.nhnacademy.bookstore.entity.cart.Cart;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface BookCartRepository extends JpaRepository<BookCart, Long> {

	@Query("select bk from BookCart bk where bk.cart.id = :cartId")
	List<BookCart> findAllByCartId(Long cartId);

	void deleteByCart(Cart cart);

	Optional<BookCart> findBookCartByBookIdAndCartId(Long bookId, Long cartId);

}
