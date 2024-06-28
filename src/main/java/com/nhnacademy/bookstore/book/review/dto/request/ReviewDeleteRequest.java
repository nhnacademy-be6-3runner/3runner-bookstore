package com.nhnacademy.bookstore.book.review.dto.request;

import lombok.Builder;

@Builder
public record ReviewDeleteRequest(
        long userId,
        String deleteReason
) {
}
