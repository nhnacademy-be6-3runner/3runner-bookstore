package com.nhnacademy.bookstore.purchase.bookCart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstore.purchase.bookCart.controller.BookCartMemberController;
import com.nhnacademy.bookstore.purchase.bookCart.dto.request.CreateBookCartMemberRequest;
import com.nhnacademy.bookstore.purchase.bookCart.dto.request.DeleteBookCartMemberRequest;
import com.nhnacademy.bookstore.purchase.bookCart.dto.request.ReadAllBookCartMemberRequest;
import com.nhnacademy.bookstore.purchase.bookCart.dto.request.UpdateBookCartMemberRequest;
import com.nhnacademy.bookstore.purchase.bookCart.dto.response.ReadAllBookCartMemberResponse;
import com.nhnacademy.bookstore.purchase.bookCart.service.BookCartMemberService;
import com.nhnacademy.bookstore.util.ApiResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookCartMemberController.class)
public class BookCartMemberControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookCartMemberService bookCartMemberService;

	@Autowired
	private ObjectMapper objectMapper;

	private CreateBookCartMemberRequest createBookCartMemberRequest;
	private UpdateBookCartMemberRequest updateBookCartMemberRequest;
	private DeleteBookCartMemberRequest deleteBookCartMemberRequest;
	private ReadAllBookCartMemberRequest readAllBookCartMemberRequest;
	private ReadAllBookCartMemberResponse readAllBookCartMemberResponse;

	@BeforeEach
	void setUp() {
		createBookCartMemberRequest = CreateBookCartMemberRequest.builder()
			.quantity(1)
			.bookId(1L)
			.userId(1L)
			.build();

		updateBookCartMemberRequest = UpdateBookCartMemberRequest.builder()
			.quantity(2)
			.bookId(1)
			.cartId(1)
			.build();

		deleteBookCartMemberRequest = DeleteBookCartMemberRequest.builder()
			.bookId(1L)
			.cartId(1L)
			.build();

		readAllBookCartMemberRequest = ReadAllBookCartMemberRequest.builder()
			.userId(1L)
			.build();

		readAllBookCartMemberResponse = ReadAllBookCartMemberResponse.builder()
			.quantity(1)
			.book(null)
			.build();
	}

	@Test
	void testCreateBookCartMember() throws Exception {
		when(bookCartMemberService.createBookCartMember(any(CreateBookCartMemberRequest.class)))
			.thenReturn(1L);

		mockMvc.perform(post("/bookstore/cart")
				.header("Member-Id", "1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createBookCartMemberRequest)))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.header.resultCode").value(201))
			.andExpect(jsonPath("$.header.successful").value(true))
			.andExpect(jsonPath("$.body.data").value(1L));
	}

	@Test
	void testReadAllBookCartMember() throws Exception {
		List<ReadAllBookCartMemberResponse> page = Collections.singletonList(readAllBookCartMemberResponse);


		when(bookCartMemberService.readAllCartMember(any(ReadAllBookCartMemberRequest.class)))
			.thenReturn(page);

		mockMvc.perform(get("/bookstore/cart")
				.header("Member-Id", "1"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.header.resultCode").value(200))
			.andExpect(jsonPath("$.header.successful").value(true))
			.andExpect(jsonPath("$.body.data[0].quantity").value(1));
	}

	@Test
	void testUpdateBookCartMember() throws Exception {
		when(bookCartMemberService.updateBookCartMember(any(UpdateBookCartMemberRequest.class)))
			.thenReturn(1L);

		mockMvc.perform(put("/bookstore/cart")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateBookCartMemberRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.data").value(1L));
	}

	@Test
	void testDeleteBookCartMember() throws Exception {
		Mockito.doNothing().when(bookCartMemberService).deleteBookCartMember(any(DeleteBookCartMemberRequest.class));

		mockMvc.perform(delete("/bookstore/cart")
				.header("Member-Id", "1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(deleteBookCartMemberRequest)))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.header.resultCode").value(204))
			.andExpect(jsonPath("$.header.successful").value(true))
			.andExpect(jsonPath("$.body.data").isEmpty());
	}
}
