package com.nhnacademy.bookstore.purchase.refundRecord.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstore.entity.refundRecord.RefundRecord;

public interface RefundRecordRepository extends JpaRepository<RefundRecord, Long> {

}
