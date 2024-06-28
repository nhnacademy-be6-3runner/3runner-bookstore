package com.nhnacademy.bookstore.book.bookCartegory.exception;

public class BookCategoryAlreadyExistsException extends RuntimeException {
    public BookCategoryAlreadyExistsException(String message) {
        super(message);
    }
}
