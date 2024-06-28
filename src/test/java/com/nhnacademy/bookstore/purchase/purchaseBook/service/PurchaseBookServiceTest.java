package com.nhnacademy.bookstore.purchase.purchaseBook.service;


import com.nhnacademy.bookstore.book.book.repository.BookRepository;
import com.nhnacademy.bookstore.entity.book.Book;
import com.nhnacademy.bookstore.entity.purchase.Purchase;
import com.nhnacademy.bookstore.entity.purchaseBook.PurchaseBook;
import com.nhnacademy.bookstore.purchase.purchase.repository.PurchaseRepository;
import com.nhnacademy.bookstore.purchase.purchaseBook.dto.request.CreatePurchaseBookRequest;
import com.nhnacademy.bookstore.purchase.purchaseBook.dto.request.DeletePurchaseBookRequest;
import com.nhnacademy.bookstore.purchase.purchaseBook.dto.request.ReadPurchaseIdRequest;
import com.nhnacademy.bookstore.purchase.purchaseBook.dto.request.UpdatePurchaseBookRequest;
import com.nhnacademy.bookstore.purchase.purchaseBook.dto.response.ReadPurchaseBookResponse;
import com.nhnacademy.bookstore.purchase.purchaseBook.repository.PurchaseBookRepository;
import com.nhnacademy.bookstore.purchase.purchaseBook.service.PurchaseBookService;
import com.nhnacademy.bookstore.purchase.purchaseBook.service.impl.PurchaseBookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PurchaseBookServiceTest {
    @Mock
    private PurchaseBookRepository purchaseBookRepository;

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private BookRepository bookRepository;


    private PurchaseBookService purchaseBookService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        purchaseBookService = new PurchaseBookServiceImpl(
                purchaseBookRepository,
                purchaseRepository,
                bookRepository
        );
    }

    @DisplayName("주문-책 생성서비스")
    @Test
    void testCreatePurchaseBook() {
        // Mock data
        CreatePurchaseBookRequest createPurchaseBookRequest = CreatePurchaseBookRequest.builder()
                .bookId(1L)
                .quantity(10)
                .price(1000)
                .purchaseId(1L)
                .build();
        Book bookMock = mock(Book.class);

        Purchase purchase = mock(Purchase.class);

        PurchaseBook purchaseBook = new PurchaseBook(bookMock,10,1000,purchase);



        // Mock repository methods
        when(bookRepository.findById(createPurchaseBookRequest.bookId())).thenReturn(Optional.of(bookMock));
        when(purchaseRepository.findById(createPurchaseBookRequest.purchaseId())).thenReturn(Optional.of(purchase));
        when(purchaseBookRepository.save(any(PurchaseBook.class))).thenReturn(purchaseBook);

        // Call service method
        Long resultId = purchaseBookService.createPurchaseBook(createPurchaseBookRequest);

        // Verify
        assertNotNull(resultId);
        assertEquals(0L, resultId);

        verify(bookRepository).findById(createPurchaseBookRequest.bookId());
        verify(purchaseRepository).findById(createPurchaseBookRequest.purchaseId());
        verify(purchaseBookRepository).save(any(PurchaseBook.class));
    }

    @DisplayName("주문-책 삭제서비스")
    @Test
    void testDeletePurchaseBook() {
        // Mock data
        DeletePurchaseBookRequest deletePurchaseBookRequest = DeletePurchaseBookRequest.builder()
                .purchaseBookId(1L)
                .build();

        // Call service method
        purchaseBookService.deletePurchaseBook(deletePurchaseBookRequest);

        // Verify
        verify(purchaseBookRepository).deleteById(deletePurchaseBookRequest.purchaseBookId());
    }


    @DisplayName("주문-책 수정서비스")
    @Test
    void testUpdatePurchaseBook() {
        // Mock data
        UpdatePurchaseBookRequest updatePurchaseBookRequest = UpdatePurchaseBookRequest.builder()
                .bookId(1L)
                .quantity(10)
                .price(1000)
                .purchaseId(1L)
                .build();

        PurchaseBook purchaseBook = new PurchaseBook(1L,null,0,0,null);


        // Mock repository method
        when(purchaseBookRepository.findByPurchaseIdAndBookId(updatePurchaseBookRequest.purchaseId(), updatePurchaseBookRequest.bookId()))
                .thenReturn(Optional.of(purchaseBook));

        // Call service method
        Long resultId = purchaseBookService.updatePurchaseBook(updatePurchaseBookRequest);

        // Verify
        assertNotNull(resultId);
        assertEquals(1L, resultId);

        verify(purchaseBookRepository).findByPurchaseIdAndBookId(updatePurchaseBookRequest.purchaseId(), updatePurchaseBookRequest.bookId());
    }

    @DisplayName("주문-책 조회서비스")
    @Test
    void testReadBookByPurchaseResponses() {
        // Mock data
        ReadPurchaseIdRequest readPurchaseIdRequest = ReadPurchaseIdRequest.builder()
                .purchaseId(1L)
                .build();
        Book bookMock = mock(Book.class);

        Purchase purchase = mock(Purchase.class);

        PurchaseBook purchaseBook = new PurchaseBook(1L, bookMock,10,1000,purchase);

        Page<PurchaseBook> purchaseBooks = new PageImpl<>(List.of(purchaseBook));

        Pageable pageable = PageRequest.of(1,10);

        // Mock repository method
        when(purchaseBookRepository.findAllByPurchaseId(readPurchaseIdRequest.purchaseId(),pageable))
                .thenReturn(purchaseBooks);

        // Call service method
        Page<ReadPurchaseBookResponse> responses = purchaseBookService.readBookByPurchaseResponses(readPurchaseIdRequest,pageable);

        // Verify
        assertNotNull(responses);
        assertEquals(1, responses.getSize());
        assertEquals(10, responses.getContent().getFirst().quantity());
        assertEquals(1000, responses.getContent().getFirst().price());

        verify(purchaseBookRepository).findAllByPurchaseId(readPurchaseIdRequest.purchaseId(),pageable);
    }
}
