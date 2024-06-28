package com.nhnacademy.bookstore.book.bookTag.exception;


public class NotExistsBookTagException extends RuntimeException {
    public NotExistsBookTagException(String message) {
        super(message);
    }
}
