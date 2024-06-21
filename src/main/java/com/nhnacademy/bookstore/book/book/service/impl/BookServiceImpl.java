package com.nhnacademy.bookstore.book.book.service.impl;

import com.nhnacademy.bookstore.book.book.dto.request.CreateBookRequest;
import com.nhnacademy.bookstore.book.book.dto.response.ReadBookResponse;
import com.nhnacademy.bookstore.book.book.exception.BookDoesNotExistException;
import com.nhnacademy.bookstore.book.book.repository.BookRepository;
import com.nhnacademy.bookstore.book.book.service.BookService;
import com.nhnacademy.bookstore.entity.book.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 책 서비스 구현체.
 *
 * @author 김병우
 */
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    /**
     * 책 등록 기능.
     *
     * @param createBookRequest createBookRequest form
     */
    // Dto -> save book
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Long createBook(CreateBookRequest createBookRequest) {
        Book book = new Book(
                createBookRequest.title(),
                createBookRequest.description(),
                createBookRequest.publishedDate(),
                createBookRequest.price(),
                createBookRequest.quantity(),
                createBookRequest.sellingPrice(),
                0,
                createBookRequest.packing(),
                createBookRequest.author(),
                createBookRequest.isbn(),
                createBookRequest.publisher(),
                null,
                null,
                null
        );
        bookRepository.save(book);
        return  book.getId();
    }

    /**
     * 책 조회 기능.
     *
     * @param bookId book entity id
     */
    @Override
    public ReadBookResponse readBookById(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new BookDoesNotExistException("책이 존재하지 않습니다"));
        return ReadBookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .description(book.getDescription())
                .publishedDate(book.getPublishedDate())
                .price(book.getPrice())
                .quantity(book.getQuantity())
                .sellingPrice(book.getSellingPrice())
                .viewCount(book.getViewCount())
                .packing(book.isPacking())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .publisher(book.getPublisher())
                .createdAt(book.getCreatedAt())
                .build();
    }
}
