package com.nhnacademy.bookstore.book.bookTag.exception;

/**
 * not found exception
 * @author 정주혁
 */
public class ReadBookTagNotFoundResponseException extends RuntimeException {
    public ReadBookTagNotFoundResponseException(String message) {
        super(message);
    }
}
