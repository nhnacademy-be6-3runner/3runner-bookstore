package com.nhnacademy.bookstore.purchase.bookCart.service.impl;

import com.nhnacademy.bookstore.book.book.exception.BookDoesNotExistException;
import com.nhnacademy.bookstore.book.book.repository.BookRepository;
import com.nhnacademy.bookstore.entity.book.Book;
import com.nhnacademy.bookstore.entity.bookCart.BookCart;
import com.nhnacademy.bookstore.entity.cart.Cart;
import com.nhnacademy.bookstore.purchase.bookCart.dto.response.ReadBookCartGuestResponse;
import com.nhnacademy.bookstore.purchase.bookCart.exception.BookCartDoesNotExistException;
import com.nhnacademy.bookstore.purchase.bookCart.repository.BookCartRedisRepository;
import com.nhnacademy.bookstore.purchase.bookCart.repository.BookCartRepository;
import com.nhnacademy.bookstore.purchase.bookCart.service.BookCartGuestService;
import com.nhnacademy.bookstore.purchase.cart.exception.CartDoesNotExistException;
import com.nhnacademy.bookstore.purchase.cart.repository.CartRepository;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import lombok.RequiredArgsConstructor;

/**
 * 도서장바구니 서비스 구현체.
 *
 * @author 김병우
 */
@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class BookCartGuestServiceImpl implements BookCartGuestService {
    private final BookCartRedisRepository bookCartRedisRepository;
    private final BookCartRepository bookCartRepository;
    private final BookRepository bookRepository;
    private final CartRepository cartRepository;

    /**
     * 도서장바구니 추가.
     *
     * @param bookId 도서아이디
     * @param quantity 수량
     * @return bookCartId
     */
    @Override
    public Long createBookCart(Long bookId, int quantity) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookDoesNotExistException("도서가 존재하지 않습니다"));

        Cart cart = new Cart();
        cartRepository.save(cart);
        long cartId = cart.getId();

        hasDataToLoad(cart.getId());

        BookCart bookCart = new BookCart(quantity, ZonedDateTime.now(), book, cart);

        bookCartRepository.save(bookCart);


        String url = "/img/no-image.png";
        if (bookCart.getBook().getBookImageList()!=null && !bookCart.getBook().getBookImageList().isEmpty()) {
            url = bookCart.getBook().getBookImageList().getFirst().getTotalImage().getUrl();
        }

        bookCartRedisRepository.create(
                Long.toString(cartId),
                bookCart.getId(),
                ReadBookCartGuestResponse.builder()
                        .bookCartId(bookCart.getId())
                        .price(book.getPrice())
                        .url(url)
                        .title(book.getTitle())
                        .quantity(bookCart.getQuantity())
                        .build()
        );

        return cartId;
    }

    /**
     * 도서장바구니 업데이트.
     *
     * @param bookId 도서아이디
     * @param cartId 장바구니아이디
     * @param quantity 수량
     * @return bookCartId
     */
    @Override
    public Long updateBookCart(Long bookId, Long cartId, int quantity) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookDoesNotExistException(bookId + "가 존재하지 않습니다"));

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartDoesNotExistException(cartId + "가 존재하지 않습니다"));


        Optional<BookCart> optionalBookCart = bookCartRepository.findByBookAndCart(book, cart);
        if (optionalBookCart.isEmpty()) {
            bookCartRepository.save(new BookCart(quantity,book,cart));
            return cartId;
        }

        hasDataToLoad(cartId);

        BookCart bookCart = optionalBookCart.get();

        int amount = bookCart.getQuantity() + quantity;
        if (amount > 0) {
            bookCart.setQuantity(amount);

            bookCartRepository.save(bookCart);
            bookCartRedisRepository.update(cartId.toString(), bookCart.getId(), amount);
        } else {
            bookCartRepository.delete(bookCart);
            bookCartRedisRepository.delete(cartId.toString(), bookCart.getId());
        }

        return cart.getId();
    }

    @Override
    public Long deleteBookCart(Long bookCartId, Long cartId) {
        BookCart bookCart = bookCartRepository.findById(bookCartId)
                .orElseThrow(() -> new BookCartDoesNotExistException("북카트 존재하지 않습니다."));

        bookCartRepository.delete(bookCart);
        bookCartRedisRepository.delete(cartId.toString(), bookCart.getId());

        return cartId;
    }

    @Override
    public Long deleteAllBookCart(Long cartId) {
        return 0L;
    }

    /**
     * 도서장바구니 목록 읽기.
     *
     * @param cartId 카트아이디
     * @return 도서장바구니 목록
     */
    @Override
    public List<ReadBookCartGuestResponse> readAllBookCart(Long cartId) {
        List<ReadBookCartGuestResponse> bookCartGuestResponseList =  bookCartRedisRepository.readAllHashName(cartId.toString());
        if (!bookCartGuestResponseList.isEmpty()) {
            return  bookCartGuestResponseList;
        }

        return  hasDataToLoad(cartId);
        //return readAllFromDb(cartId);
    }

    /**
     * 레디스 데이터 업데이트 메서드.
     *
     * @param cartId 장바구니아이디
     * @return 도서장바구니 목록
     */
    private List<ReadBookCartGuestResponse> hasDataToLoad(Long cartId) {
        List<ReadBookCartGuestResponse> readBookCartGuestResponses = readAllFromDb(cartId);
        if (bookCartRedisRepository.isMiss(cartId.toString()) && !readBookCartGuestResponses.isEmpty()) {
            bookCartRedisRepository.loadData(readBookCartGuestResponses, cartId.toString());
        }
        return readBookCartGuestResponses;
    }

    /**
     * DB에서 데이터 목록 불러오기.
     *
     * @param cartId 카트 아이디
     * @return DB 도서 장바구니 목록
     */
    private List<ReadBookCartGuestResponse> readAllFromDb(Long cartId) {
        List<BookCart> list = bookCartRepository.findAllByCartId(cartId);
        List<ReadBookCartGuestResponse> listDto = new ArrayList<>();
        log.info("{}", list);
        for (BookCart bookCart : list) {
            String url = "/img/no-image.png";
            if (bookCart.getBook().getBookImageList()!=null && !bookCart.getBook().getBookImageList().isEmpty()) {
                url = bookCart.getBook().getBookImageList().getFirst().getTotalImage().getUrl();
            }

            listDto.add(ReadBookCartGuestResponse.builder()
                    .bookCartId(bookCart.getId())
                    .bookId(bookCart.getBook().getId())
                    .price(bookCart.getBook().getPrice())
                    .url(url)
                    .title(bookCart.getBook().getTitle())
                    .quantity(bookCart.getQuantity())
                    .build());
        }

        return listDto;
    }
}
