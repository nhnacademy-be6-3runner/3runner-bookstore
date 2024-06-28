package com.nhnacademy.bookstore.book.book.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.ZonedDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import com.nhnacademy.bookstore.BaseDocumentTest;
import com.nhnacademy.bookstore.book.book.dto.response.ReadBookResponse;
import com.nhnacademy.bookstore.book.book.service.BookService;
import com.nhnacademy.bookstore.book.bookCartegory.service.BookCategoryService;
import com.nhnacademy.bookstore.book.bookImage.service.BookImageService;
import com.nhnacademy.bookstore.book.bookTag.dto.request.ReadBookIdRequest;
import com.nhnacademy.bookstore.book.bookTag.dto.response.ReadTagByBookResponse;
import com.nhnacademy.bookstore.book.bookTag.service.BookTagService;
import com.nhnacademy.bookstore.book.category.dto.response.CategoryParentWithChildrenResponse;

class BookControllerTest extends BaseDocumentTest {

	@MockBean
	private BookService bookService;
	@MockBean
	private BookImageService bookImageService;
	@MockBean
	private BookTagService bookTagService;
	@MockBean
	private BookCategoryService bookCategoryService;

	@Test
	void createBook() {
	}

	@DisplayName("책 디테일 뷰 가져오기")
	@Test
	void readBook() throws Exception {

		CategoryParentWithChildrenResponse categoryParentWithChildrenResponse1 = CategoryParentWithChildrenResponse.builder()
			.id(1L)
			.name("Test Category1")
			.build();
		CategoryParentWithChildrenResponse categoryParentWithChildrenResponse2 = CategoryParentWithChildrenResponse.builder()
			.id(2L)
			.name("Test Category1")
			.childrenList(List.of(categoryParentWithChildrenResponse1))
			.build();

		ReadBookResponse readBookResponse = ReadBookResponse.builder()
			.id(1L)
			.title("test Title")
			.description("Test description")
			.publishedDate(ZonedDateTime.now())
			.price(10000)
			.quantity(10)
			.sellingPrice(10000)
			.viewCount(777)
			.packing(true)
			.author("Test Author")
			.isbn("1234567890123")
			.publisher("Test Publisher")
			.imagePath("Test Image Path")

			.build();

		given(bookService.readBookById(anyLong())).willReturn(readBookResponse);
		given(bookCategoryService.readBookWithCategoryList(anyLong())).willReturn(
			List.of(categoryParentWithChildrenResponse2));
		given(bookTagService.readTagByBookId(any(ReadBookIdRequest.class))).willReturn(
			List.of(ReadTagByBookResponse.builder().id(1L).name("Test tag").build()));

		this.mockMvc.perform(RestDocumentationRequestBuilders.get("/bookstore/books/{bookId}", 1L)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andDo(document(snippetPath,
				"아이디 기반 멤버 정보를 조회하는 API",
				pathParameters(
					parameterWithName("bookId").description("책 아이디")
				),
				responseFields(
					fieldWithPath("header.resultCode").type(JsonFieldType.NUMBER).description("결과 코드"),
					fieldWithPath("header.successful").type(JsonFieldType.BOOLEAN).description("성공 여부"),
					fieldWithPath("body.data.id").type(JsonFieldType.NUMBER).description("책 아이디"),
					fieldWithPath("body.data.title").type(JsonFieldType.STRING).description("책 제목"),
					fieldWithPath("body.data.description").type(JsonFieldType.STRING).description("책 설명"),
					fieldWithPath("body.data.publishedDate").type(JsonFieldType.STRING).description("출판 날짜"),
					fieldWithPath("body.data.price").type(JsonFieldType.NUMBER).description("책 가격"),
					fieldWithPath("body.data.quantity").type(JsonFieldType.NUMBER).description("수량"),
					fieldWithPath("body.data.sellingPrice").type(JsonFieldType.NUMBER).description("판매 가격"),
					fieldWithPath("body.data.viewCount").type(JsonFieldType.NUMBER).description("조회수"),
					fieldWithPath("body.data.packing").type(JsonFieldType.BOOLEAN).description("포장 여부"),
					fieldWithPath("body.data.author").type(JsonFieldType.STRING).description("저자"),
					fieldWithPath("body.data.isbn").type(JsonFieldType.STRING).description("ISBN 번호"),
					fieldWithPath("body.data.imagePath").type(JsonFieldType.STRING).description("책의 메인 이미지"),
					fieldWithPath("body.data.publisher").type(JsonFieldType.STRING).description("책의 출판사"),
					fieldWithPath("body.data.categoryList").type(JsonFieldType.ARRAY).description("카테고리 리스트"),
					fieldWithPath("body.data.categoryList[].id").type(JsonFieldType.NUMBER).description("카테고리 아이디"),
					fieldWithPath("body.data.categoryList[].name").type(JsonFieldType.STRING).description("카테고리 이름"),
					fieldWithPath("body.data.categoryList[].childrenList").type(JsonFieldType.ARRAY)
						.description("하위 카테고리 리스트"),
					fieldWithPath("body.data.categoryList[].childrenList[].id").type(JsonFieldType.NUMBER)
						.description("하위 카테고리 아이디"),
					fieldWithPath("body.data.categoryList[].childrenList[].name").type(JsonFieldType.STRING)
						.description("하위 카테고리 이름"),
					fieldWithPath("body.data.categoryList[].childrenList[].childrenList").type(JsonFieldType.NULL)
						.description("더 하위 카테고리 리스트"),
					fieldWithPath("body.data.tagList").type(JsonFieldType.ARRAY).description("태그 리스트"),
					fieldWithPath("body.data.tagList[].id").type(JsonFieldType.NUMBER).description("태그 아이디"),
					fieldWithPath("body.data.tagList[].name").type(JsonFieldType.STRING).description("태그 이름")

				)
			));
	}
}