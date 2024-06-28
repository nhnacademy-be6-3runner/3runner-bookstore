package com.nhnacademy.bookstore.purchase.bookCart.exception;

public class BookCartArgumentErrorException extends RuntimeException{
    public BookCartArgumentErrorException(String message) {
        super(message);
    }
}
