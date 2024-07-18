package com.nhnacademy.bookstore.purchase.refundRecord.exception;

public class NotExistsRefundRecordRedis
	extends RuntimeException {
	public NotExistsRefundRecordRedis() {
		super("주문내역이 redis에 존재하지 않습니다.");
	}
}
