package com.nhnacademy.bookstore.purchase.bookCart.exception;

public class BookCartDoesNotExistException extends RuntimeException{
    public BookCartDoesNotExistException(String message) {
        super(message);
    }
}
