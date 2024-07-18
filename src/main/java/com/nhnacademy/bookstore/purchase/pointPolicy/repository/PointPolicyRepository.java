package com.nhnacademy.bookstore.purchase.pointPolicy.repository;

import com.nhnacademy.bookstore.entity.pointPolicy.PointPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointPolicyRepository extends JpaRepository<PointPolicy, Long> {
    Optional<PointPolicy> findByPolicyName(String policyName);
}
