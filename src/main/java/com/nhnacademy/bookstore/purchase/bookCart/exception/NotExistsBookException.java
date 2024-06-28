package com.nhnacademy.bookstore.purchase.bookCart.exception;

public class NotExistsBookException extends RuntimeException {
	public NotExistsBookException() {
		super("존재하지 않는 책입니다.");
	}
}
