package com.nhnacademy.bookstore.book.category.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCategoryRequest {
    @NotBlank
    private String name;
    @Setter
    private Long parentId;
}
