package com.nhnacademy.bookstore.book.bookCartegory.exception;

public class BookCategoryNotFoundException extends RuntimeException {
    public BookCategoryNotFoundException(String message) {
        super(message);
    }
}
