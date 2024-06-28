package com.nhnacademy.bookstore.purchase.bookCart.dto.response;

import java.time.ZonedDateTime;

import lombok.Builder;

@Builder
public record ReadBookCartBook(
	String title,
	String description,
	ZonedDateTime publishedDate,
	int price,
	int quantity,
	int sellingPrice,
	boolean packing,
	String author,
	String publisher
) {
}
