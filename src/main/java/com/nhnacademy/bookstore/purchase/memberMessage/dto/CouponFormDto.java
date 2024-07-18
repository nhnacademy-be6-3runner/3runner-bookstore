package com.nhnacademy.bookstore.purchase.memberMessage.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record CouponFormDto(Long id, String name) {
    }
