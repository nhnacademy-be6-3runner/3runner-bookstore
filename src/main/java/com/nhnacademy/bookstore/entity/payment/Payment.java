package com.nhnacademy.bookstore.entity.payment;

import com.nhnacademy.bookstore.entity.purchase.Purchase;
import com.nhnacademy.bookstore.entity.payment.enums.PaymentStatus;
import com.nhnacademy.bookstore.entity.paymentType.PaymentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private ZonedDateTime paidAt;

    private PaymentStatus paymentStatus;

    @ManyToOne
    private Purchase purchase;

    @ManyToOne
    private PaymentType paymentType;
}
