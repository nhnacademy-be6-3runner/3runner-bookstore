package com.nhnacademy.bookstore.book.refund.repository;

import com.nhnacademy.bookstore.entity.refund.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund, Long> {
}
