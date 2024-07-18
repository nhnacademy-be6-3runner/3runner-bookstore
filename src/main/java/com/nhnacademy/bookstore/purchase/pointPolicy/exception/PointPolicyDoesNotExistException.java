package com.nhnacademy.bookstore.purchase.pointPolicy.exception;

public class PointPolicyDoesNotExistException extends RuntimeException{
    public PointPolicyDoesNotExistException(String message) {
        super(message);
    }
}
