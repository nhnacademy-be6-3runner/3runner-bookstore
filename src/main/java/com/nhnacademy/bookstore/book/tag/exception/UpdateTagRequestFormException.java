package com.nhnacademy.bookstore.book.tag.exception;

/**
 * 태그 수정 request exception
 * @author 정주혁
 */
public class UpdateTagRequestFormException extends RuntimeException {
    public UpdateTagRequestFormException(String message) {
        super(message);
    }
}
