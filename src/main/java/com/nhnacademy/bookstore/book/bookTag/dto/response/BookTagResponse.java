package com.nhnacademy.bookstore.book.bookTag.dto.response;

import lombok.Builder;

@Builder
public record BookTagResponse(
    long id,
    String name
) {}
