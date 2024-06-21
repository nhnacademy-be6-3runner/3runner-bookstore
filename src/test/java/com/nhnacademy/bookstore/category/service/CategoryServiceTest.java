package com.nhnacademy.bookstore.category.service;

import com.nhnacademy.bookstore.book.category.dto.request.CreateCategoryRequest;
import com.nhnacademy.bookstore.book.category.dto.request.UpdateCategoryRequest;
import com.nhnacademy.bookstore.book.category.exception.CategoryNotFoundException;
import com.nhnacademy.bookstore.book.category.exception.DuplicateCategoryNameException;
import com.nhnacademy.bookstore.book.category.repository.CategoryRepository;
import com.nhnacademy.bookstore.book.category.service.impl.CategoryServiceImpl;
import com.nhnacademy.bookstore.entity.category.Category;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    Category parent;

    @BeforeEach
    void setUp() {

    }

    @DisplayName("상위 카테고리 생성 테스트")
    @Test
    void createTopLevelCategory() {
        CreateCategoryRequest dto = new CreateCategoryRequest("상위 카테고리", null);
        Category category = Category.builder()
                .name("상위 카테고리")
                .build();

        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        categoryService.createCategory(dto);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @DisplayName("카테고리 삭제 테스트")
    @Test
    void deleteCategory() {
        Category category = Category.builder()
                .id(1L)
                .name("category")
                .build();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).delete(any(Category.class));
    }

    @DisplayName("중복된 이름 카테고리 생성 테스트")
    @Test
    void createCategory_DuplicateName_Exception() {
        CreateCategoryRequest dto = new CreateCategoryRequest("test", null);
        when(categoryRepository.existsByName(any())).thenReturn(true);

        assertThatThrownBy(() -> categoryService.createCategory(dto))
                .isInstanceOf(DuplicateCategoryNameException.class);
    }

    @DisplayName("존재하지 않는 카테고리 수정 테스트")
    @Test
    void updateCategory_CategoryNotFound_Exception() {
        long id = 1L;
        UpdateCategoryRequest dto = new UpdateCategoryRequest("test", null);
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.updateCategory(id, dto))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @DisplayName("존재하지 않는 카테고리 삭제 테스트")
    @Test
    void deleteCategory_CategoryNotFound_Exception() {
        long id = 1L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.deleteCategory(id))
                .isInstanceOf(CategoryNotFoundException.class);
    }
}

