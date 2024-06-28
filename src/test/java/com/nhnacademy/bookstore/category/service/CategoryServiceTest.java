package com.nhnacademy.bookstore.category.service;

import com.nhnacademy.bookstore.book.category.dto.request.CreateCategoryRequest;
import com.nhnacademy.bookstore.book.category.dto.request.UpdateCategoryRequest;
import com.nhnacademy.bookstore.book.category.dto.response.CategoryParentWithChildrenResponse;
import com.nhnacademy.bookstore.book.category.exception.CategoryNotFoundException;
import com.nhnacademy.bookstore.book.category.exception.DuplicateCategoryNameException;
import com.nhnacademy.bookstore.book.category.repository.CategoryRepository;
import com.nhnacademy.bookstore.book.category.service.impl.CategoryServiceImpl;
import com.nhnacademy.bookstore.entity.category.Category;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @DisplayName("상위 카테고리 생성 테스트")
    @Test
    void createTopLevelCategory() {
        CreateCategoryRequest dto = new CreateCategoryRequest("상위 카테고리", null);
        Category category = new Category();
        category.setName("상위 카테고리");

        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        categoryService.createCategory(dto);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @DisplayName("카테고리 삭제 테스트")
    @Test
    void deleteCategory() {
        Category category = new Category();
        category.setName("category");

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        categoryService.deleteCategory(category.getId());

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

    @DisplayName("부모 ID로 자식 카테고리 조회 테스트")
    @Test
    void testGetChildrenCategoriesByParentId() {
        long parentId = 1L;

        Category parentCategory = new Category();
        parentCategory.setId(parentId);
        parentCategory.setName("Parent Category");

        Category childCategory1 = new Category();
        childCategory1.setId(2L);
        childCategory1.setName("Child Category 1");
        childCategory1.setParent(parentCategory);

        Category childCategory2 = new Category();
        childCategory2.setId(3L);
        childCategory2.setName("Child Category 2");
        childCategory2.setParent(parentCategory);

        Category grandChildCategory = new Category();
        grandChildCategory.setId(4L);
        grandChildCategory.setName("Grand Child Category");
        grandChildCategory.setParent(childCategory1);

        childCategory1.setChildren(Collections.singletonList(grandChildCategory));
        parentCategory.setChildren(Arrays.asList(childCategory1, childCategory2));

        when(categoryRepository.existsById(parentId)).thenReturn(true);
        when(categoryRepository.findChildrenCategoriesByParentId(parentId)).thenReturn(
                Arrays.asList(
                        new CategoryParentWithChildrenResponse(childCategory1.getId(), childCategory1.getName(),
                                Collections.singletonList(new CategoryParentWithChildrenResponse(grandChildCategory.getId(), grandChildCategory.getName(), Collections.emptyList()))),
                        new CategoryParentWithChildrenResponse(childCategory2.getId(), childCategory2.getName(), Collections.emptyList())
                )
        );

        List<CategoryParentWithChildrenResponse> result = categoryService.getChildrenCategoriesByParentId(parentId);

        assertNotNull(result);
        assertEquals(2, result.size());

        // 자식 카테고리 1 검증
        CategoryParentWithChildrenResponse childResponse1 = result.get(0);
        assertEquals("Child Category 1", childResponse1.getName());

        // 자식 카테고리 1의 자식 (손자 카테고리) 검증
        assertEquals(1, childResponse1.getChildrenList().size());
        CategoryParentWithChildrenResponse grandChildResponse = childResponse1.getChildrenList().get(0);
        assertEquals("Grand Child Category", grandChildResponse.getName());

        // 자식 카테고리 2 검증
        CategoryParentWithChildrenResponse childResponse2 = result.get(1);
        assertEquals("Child Category 2", childResponse2.getName());

        verify(categoryRepository, times(1)).existsById(parentId);
        verify(categoryRepository, times(1)).findChildrenCategoriesByParentId(parentId);
    }
}
