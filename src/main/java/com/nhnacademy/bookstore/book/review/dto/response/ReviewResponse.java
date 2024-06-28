package com.nhnacademy.bookstore.book.review.dto.response;

import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record ReviewResponse(
        long reviewId,
        String title,
        String imgUrl, // 메인 사진
        double rating,
        long userEmail,
        ZonedDateTime createdAt
) {
}
