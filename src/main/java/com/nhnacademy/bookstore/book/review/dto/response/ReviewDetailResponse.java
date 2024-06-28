package com.nhnacademy.bookstore.book.review.dto.response;

import lombok.Builder;

import java.time.ZonedDateTime;


@Builder
public record ReviewDetailResponse(
        long reviewId,
        String title,
        String content,
        double ratings,
        long userEmail,
        ZonedDateTime createdAt,
        boolean updated, // 수정 여부
        ZonedDateTime updatedAt // 수정 시간
) {
}
