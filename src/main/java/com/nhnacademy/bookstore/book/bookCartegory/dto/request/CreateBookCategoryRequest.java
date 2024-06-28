package com.nhnacademy.bookstore.book.bookCartegory.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record CreateBookCategoryRequest(long bookId, List<Long> categoryIds) {
}
