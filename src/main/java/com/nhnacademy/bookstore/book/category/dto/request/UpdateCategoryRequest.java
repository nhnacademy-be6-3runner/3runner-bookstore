package com.nhnacademy.bookstore.book.category.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCategoryRequest {
    private String name;
    @Setter
    private Long parentId;
}
