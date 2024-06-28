package com.nhnacademy.bookstore.purchase.bookCart.exception;

public class UpdateBookCartMemberRequestFormException extends RuntimeException {
	public UpdateBookCartMemberRequestFormException() {
		super("수정 request 폼 오류");
	}
}
