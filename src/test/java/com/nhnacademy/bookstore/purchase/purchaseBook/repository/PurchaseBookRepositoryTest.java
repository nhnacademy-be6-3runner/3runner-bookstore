package com.nhnacademy.bookstore.purchase.purchaseBook.repository;

import com.nhnacademy.bookstore.entity.book.Book;
import com.nhnacademy.bookstore.entity.purchase.Purchase;
import com.nhnacademy.bookstore.entity.purchase.enums.MemberType;
import com.nhnacademy.bookstore.entity.purchase.enums.PurchaseStatus;
import com.nhnacademy.bookstore.entity.purchaseBook.PurchaseBook;
import com.nhnacademy.bookstore.purchase.purchase.repository.PurchaseRepository;
import com.nhnacademy.bookstore.book.book.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // 각 테스트 메서드 실행 후에 컨텍스트를 정리
public class PurchaseBookRepositoryTest {

    @Autowired
    private PurchaseBookRepository purchaseBookRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    private Book book;
    private Purchase purchase;
    private PurchaseBook purchaseBook;

    @BeforeEach
    public void setup() {
        // Book 초기화
        book = new Book(
                "Sample Book Title",
                "This is a description of the sample book.",
                ZonedDateTime.now().minusMonths(2), // published date
                1500, // price
                100, // quantity
                1200, // selling price
                0, // view count
                true, // packing
                "Sample Author",
                "1234567890123", // ISBN
                "Sample Publisher",
                null,
                null,
                null // book images
        );
        bookRepository.save(book);

        // Purchase 초기화
        purchase = new Purchase(
                1L,
                UUID.randomUUID(),
                PurchaseStatus.SHIPPED,
                10,
                10,
                ZonedDateTime.now(),
                "hhh",
                null,
                MemberType.MEMBER,
                null,
                null,
                null,
                null
        );
        purchaseRepository.save(purchase);

        // PurchaseBook 초기화
        purchaseBook = new PurchaseBook(book, 10, 1000, purchase);
        purchaseBookRepository.save(purchaseBook);
    }

    @DisplayName("주문id와 책id로 주문-책 조회 쿼리 테스트")
    @Test
    public void testFindByPurchaseIdAndBookId() {
        PurchaseBook foundPurchaseBook = purchaseBookRepository.findByPurchaseIdAndBookId(purchase.getId(), book.getId()).orElse(null);
        assertNotNull(foundPurchaseBook);
        assertEquals(purchaseBook, foundPurchaseBook);
    }

    @DisplayName("주문id로 주문-책 조회 쿼리 테스트")
    @Test
    public void testFindAllByPurchaseId() {
        Pageable pageable = PageRequest.of(1,10);
        Page<PurchaseBook> purchaseBooks = purchaseBookRepository.findAllByPurchaseId(purchase.getId(), pageable);

        assertNotNull(purchaseBooks);
        assertEquals(10, purchaseBooks.getSize());

    }
}
