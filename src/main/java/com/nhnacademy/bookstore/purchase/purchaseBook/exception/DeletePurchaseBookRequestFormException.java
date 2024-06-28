package com.nhnacademy.bookstore.purchase.purchaseBook.exception;


import org.springframework.validation.BindingResult;

/**
 * 삭제 dto가 잘못됐을경우 exception
 *
 * @author 정주혁
 */
public class DeletePurchaseBookRequestFormException extends RuntimeException {
    public DeletePurchaseBookRequestFormException(BindingResult message) {
        super(message.getFieldErrors().toString());
    }
}
