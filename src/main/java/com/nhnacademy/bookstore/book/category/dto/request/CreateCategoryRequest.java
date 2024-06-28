package com.nhnacademy.bookstore.book.category.dto.request;


import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCategoryRequest {
    private String name;
    @Setter
    private Long parentId;
}
