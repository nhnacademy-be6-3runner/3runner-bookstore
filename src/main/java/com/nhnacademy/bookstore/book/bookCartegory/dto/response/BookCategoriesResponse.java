package com.nhnacademy.bookstore.book.bookCartegory.dto.response;

import lombok.Builder;

/**
 * 도서에 해당하는 카테고리 조회 dto
 * @author 김은비
 * @param categoryName
 */
@Builder
public record BookCategoriesResponse(long id, String categoryName, Long parentId) {
}
