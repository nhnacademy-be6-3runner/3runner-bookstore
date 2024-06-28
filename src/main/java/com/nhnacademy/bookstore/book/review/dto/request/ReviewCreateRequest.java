package com.nhnacademy.bookstore.book.review.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record ReviewCreateRequest(
        @Size(min = 1, max = 50) String title,
        @Size(min = 1, max = 200) String content,
        @NotNull double ratings,
        List<String> imageList
) {
}
