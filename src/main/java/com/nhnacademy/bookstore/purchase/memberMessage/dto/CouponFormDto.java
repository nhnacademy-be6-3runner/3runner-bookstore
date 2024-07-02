package com.nhnacademy.bookstore.purchase.memberMessage.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record CouponFormDto(Long id, ZonedDateTime startDate, ZonedDateTime endDate, ZonedDateTime createdAt, String name,
                            UUID code, Integer maxPrice, Integer minPrice) {
    }
