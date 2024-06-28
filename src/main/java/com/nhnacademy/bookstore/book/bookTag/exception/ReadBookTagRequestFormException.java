package com.nhnacademy.bookstore.book.bookTag.exception;

/**
 * 잘못된 Request값 전달 exception
 * @author 정주혁
 */
public class ReadBookTagRequestFormException extends RuntimeException {
    public ReadBookTagRequestFormException(String message) {
        super(message);
    }
}
