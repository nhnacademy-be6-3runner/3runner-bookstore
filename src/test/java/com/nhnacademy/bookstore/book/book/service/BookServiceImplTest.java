package com.nhnacademy.bookstore.book.book.service;

import com.nhnacademy.bookstore.book.book.dto.request.CreateBookRequest;
import com.nhnacademy.bookstore.book.book.dto.response.ReadBookResponse;
import com.nhnacademy.bookstore.book.book.exception.BookDoesNotExistException;
import com.nhnacademy.bookstore.book.book.repository.BookRepository;
import com.nhnacademy.bookstore.book.book.service.impl.BookServiceImpl;
import com.nhnacademy.bookstore.entity.book.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void testCreateBook() {
        CreateBookRequest request = new CreateBookRequest(
                "Test Title",
                "Test Description",
                ZonedDateTime.now(),
                1000,
                10,
                900,
                true,
                "Test Author",
                "123456789",
                "Test Publisher",
                null,
                null,
                null,
                null
        );

        bookService.createBook(request);

        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void testReadBookById_Success() {
        Book book = new Book(
                "Test Title",
                "Test Description",
                ZonedDateTime.now(),
                1000,
                10,
                900,
                0,
                true,
                "Test Author",
                "123456789",
                "Test Publisher",
                null,
                null,
                null
        );
        ReadBookResponse bookResponse = new ReadBookResponse(
                1l,
                "Test Title",
                "Test Description",
                ZonedDateTime.now(),
                1000,
                10,
                900,
                0,
                true,
                "Test Author",
                "123456789",
                "Test Publisher",
                ZonedDateTime.now()
        );
        book.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        ReadBookResponse foundBook = bookService.readBookById(1L);

        assertEquals(book.getId(), foundBook.id());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    public void testReadBookById_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookDoesNotExistException.class, () -> bookService.readBookById(1L));

        verify(bookRepository, times(1)).findById(1L);
    }
}