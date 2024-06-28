package com.nhnacademy.bookstore.book.reviewImage.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateReviewImageRequest(
        @NotNull @NotBlank(message = "url is empty") String url,
        long reviewId) {
}
