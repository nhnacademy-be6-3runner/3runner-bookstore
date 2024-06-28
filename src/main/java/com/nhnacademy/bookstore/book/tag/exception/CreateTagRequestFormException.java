package com.nhnacademy.bookstore.book.tag.exception;


/**
 * 태그 추가 exception
 * @author 정주혁
 */
public class CreateTagRequestFormException extends RuntimeException {
    public CreateTagRequestFormException(String message) {
        super(message);
    }
}
