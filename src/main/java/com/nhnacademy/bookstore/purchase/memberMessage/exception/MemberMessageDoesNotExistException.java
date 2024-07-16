package com.nhnacademy.bookstore.purchase.memberMessage.exception;

public class MemberMessageDoesNotExistException extends RuntimeException{
    public MemberMessageDoesNotExistException(String message) {
        super(message);
    }
}
