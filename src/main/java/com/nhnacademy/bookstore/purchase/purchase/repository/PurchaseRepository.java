package com.nhnacademy.bookstore.purchase.purchase.repository;

import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.entity.purchase.Purchase;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    Boolean existsPurchaseByOrderNumber(@NotNull UUID orderNumber);
    List<Purchase> findByMember(Member member);
    Optional<Purchase> findPurchaseByOrderNumber(@NotNull UUID orderNumber);
}
