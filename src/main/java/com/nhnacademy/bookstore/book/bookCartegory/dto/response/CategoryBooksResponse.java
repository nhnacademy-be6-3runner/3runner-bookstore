package com.nhnacademy.bookstore.book.bookCartegory.dto.response;

import com.nhnacademy.bookstore.entity.category.Category;

/**
 * 카테고리로 도서 조회하기
 * @param title
 * @param price
 * @param sellingPrice
 * @param author
 */
public record CategoryBooksResponse(
        String title, int price, int sellingPrice, String author
) {
}
