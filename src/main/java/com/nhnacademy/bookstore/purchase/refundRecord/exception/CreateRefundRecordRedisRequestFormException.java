package com.nhnacademy.bookstore.purchase.refundRecord.exception;

public class CreateRefundRecordRedisRequestFormException
	extends RuntimeException {
	public CreateRefundRecordRedisRequestFormException() {
		super("환불 내역 생성 request 오류");
	}
}
