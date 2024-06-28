// package com.nhnacademy.bookstore.category.controller;
//
// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.BDDMockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
// import java.util.Collections;
//
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.nhnacademy.bookstore.book.category.controller.CategoryController;
// import com.nhnacademy.bookstore.book.category.dto.request.CreateCategoryRequest;
// import com.nhnacademy.bookstore.book.category.dto.response.CategoryResponse;
// import com.nhnacademy.bookstore.book.category.service.CategoryService;
//
// @WebMvcTest(CategoryController.class)
// public class CategoryControllerTest {
//
// 	@Autowired
// 	private MockMvc mockMvc;
//
// 	@MockBean
// 	private CategoryService categoryService;
//
// 	@Autowired
// 	private ObjectMapper objectMapper;
//
// 	@DisplayName("카테고리 생성 테스트")
// 	@Test
// 	void createCategoryTest() throws Exception {
// 		CreateCategoryRequest createCategoryRequest = CreateCategoryRequest.builder()
// 			.name("새 카테고리")
// 			.build();
//
// 		mockMvc.perform(post("/bookstore/api/categories")
// 				.contentType(MediaType.APPLICATION_JSON)
// 				.content(objectMapper.writeValueAsString(createCategoryRequest)))
// 			.andExpect(status().isCreated())
// 			.andExpect(jsonPath("$.header.successful").value(true))
// 			.andExpect(jsonPath("$.header.resultCode").value(201));
// 	}
//
// 	@DisplayName("단일 카테고리 조회 테스트")
// 	@Test
// 	void readCategoryTest() throws Exception {
// 		CategoryResponse categoryResponse = CategoryResponse.builder()
// 			.id(1L)
// 			.name("카테고리")
// 			.build();
//
// 		given(categoryService.getCategory(anyLong())).willReturn(categoryResponse);
//
// 		mockMvc.perform(get("/bookstore/api/categories/{categoryId}", 1L))
// 			.andExpect(status().isOk())
// 			.andExpect(jsonPath("$.header.successful").value(true))
// 			.andExpect(jsonPath("$.header.resultCode").value(200))
// 			.andExpect(jsonPath("$.body.data.id").value(1L))
// 			.andExpect(jsonPath("$.body.data.name").value("카테고리"));
// 	}
//
// 	// @DisplayName("모든 카테고리 조회 테스트")
// 	// @Test
// 	// void readAllCategoriesTest() throws Exception {
// 	//     CategoryResponse categoryResponse = CategoryResponse.builder()
// 	//             .id(1L)
// 	//             .name("카테고리")
// 	//             .build();
// 	//
// 	//     given(categoryService.getCategories()).willReturn(
// 	//             Collections.singletonList(categoryResponse));
// 	//
// 	//     mockMvc.perform(get("/bookstore/api/categories"))
// 	//             .andExpect(status().isOk())
// 	//             .andExpect(jsonPath("$.header.successful").value(true))
// 	//             .andExpect(jsonPath("$.header.resultCode").value(200))
// 	//             .andExpect(jsonPath("$.body.data[0].id").value(1L))
// 	//             .andExpect(jsonPath("$.body.data[0].name").value("카테고리"));
// 	// }
//
// 	@DisplayName("상위 카테고리 조회 테스트")
// 	@Test
// 	void readAllParentCategoriesTest() throws Exception {
// 		CategoryResponse categoryResponse = CategoryResponse.builder()
// 			.id(1L)
// 			.name("부모 카테고리")
// 			.build();
//
// 		given(categoryService.getParentCategories()).willReturn(
// 			Collections.singletonList(categoryResponse));
//
// 		mockMvc.perform(get("/bookstore/api/categories/parents"))
// 			.andExpect(status().isOk())
// 			.andExpect(jsonPath("$.header.successful").value(true))
// 			.andExpect(jsonPath("$.header.resultCode").value(200))
// 			.andExpect(jsonPath("$.body.data[0].id").value(1L))
// 			.andExpect(jsonPath("$.body.data[0].name").value("부모 카테고리"));
// 	}
// }