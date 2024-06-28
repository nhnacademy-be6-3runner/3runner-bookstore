package com.nhnacademy.bookstore.purchase.purchaseBook.exception;


import org.springframework.validation.BindingResult;

/**
 * 조회 dto가 잘못됐을경우 exception
 *
 * @author 정주혁
 */
public class ReadPurchaseBookRequestFormException extends RuntimeException {
    public ReadPurchaseBookRequestFormException(BindingResult message) {
        super(message.getFieldErrors().toString());
    }
}
