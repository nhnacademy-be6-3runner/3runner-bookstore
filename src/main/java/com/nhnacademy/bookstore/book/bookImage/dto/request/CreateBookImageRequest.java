package com.nhnacademy.bookstore.book.bookImage.dto.request;

import com.nhnacademy.bookstore.entity.bookImage.enums.BookImageType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateBookImageRequest(
	@NotNull
	@NotBlank
	@Size(max = 50)
	String url,
	@NotNull
	BookImageType type,
	long bookId) {
}