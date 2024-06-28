package com.nhnacademy.bookstore.purchase.bookCart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstore.purchase.bookCart.dto.request.CreateBookCartRequest;
import com.nhnacademy.bookstore.purchase.bookCart.dto.request.DeleteBookCartRequest;
import com.nhnacademy.bookstore.purchase.bookCart.dto.request.ReadAllBookCartMemberRequest;
import com.nhnacademy.bookstore.purchase.bookCart.dto.request.UpdateBookCartRequest;
import com.nhnacademy.bookstore.purchase.bookCart.dto.response.ReadAllBookCartMemberResponse;
import com.nhnacademy.bookstore.purchase.bookCart.service.BookCartGuestService;
import com.nhnacademy.bookstore.purchase.bookCart.service.BookCartMemberService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BookCartController.class)
class BookCartGuestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookCartGuestService bookCartGuestService;

    @MockBean
    private BookCartMemberService bookCartMemberService;

    private CreateBookCartRequest createBookCartRequest;
	private UpdateBookCartRequest updateBookCartRequest;
	private DeleteBookCartRequest deleteBookCartRequest;
	private ReadAllBookCartMemberRequest readAllBookCartMemberRequest;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
	void setUp() {
		createBookCartRequest = CreateBookCartRequest.builder()
			.quantity(1)
			.bookId(1L)
			.userId(1L)
			.build();

        updateBookCartRequest = UpdateBookCartRequest.builder()
			.quantity(2)
			.bookId(1)
			.cartId(1)
			.build();

        deleteBookCartRequest = DeleteBookCartRequest.builder()
			.bookCartId(1L)
            .cartId(1L)
			.build();

		readAllBookCartMemberRequest = ReadAllBookCartMemberRequest.builder()
			.userId(1L)
			.build();

	}

    @Test
    void testGetCart() throws Exception {
        when(bookCartGuestService.readAllBookCart(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/bookstore/carts/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.successful").value(true))
                .andExpect(jsonPath("$.body.data").isArray());
    }
    @Test
	void testReadAllBookCartMember() throws Exception {
        when(bookCartMemberService.readAllCartMember(readAllBookCartMemberRequest)).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/bookstore/carts")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Member-Id", 1L)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.successful").value(true))
                .andExpect(jsonPath("$.body.data").isArray());
	}

    @Test
    void testAddCart() throws Exception {
        when(bookCartGuestService.createBookCart(anyLong(), anyInt())).thenReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/bookstore/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"bookId\":1,\"quantity\":1}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.successful").value(true));

        verify(bookCartGuestService, times(1)).createBookCart(anyLong(),anyInt());
    }

    @Test
	void testCreateBookCartMember() throws Exception {
		when(bookCartMemberService.createBookCartMember(any(CreateBookCartRequest.class)))
			.thenReturn(1L);

		mockMvc.perform(MockMvcRequestBuilders.post("/bookstore/carts")
				.contentType(MediaType.APPLICATION_JSON)
                .header("Member-Id", 1L)
				.content(objectMapper.writeValueAsString(createBookCartRequest)))
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.header.resultCode").value(201))
			.andExpect(jsonPath("$.header.successful").value(true))
			.andExpect(jsonPath("$.body.data").value(1L));
	}

    @Test
    void testUpdateCart() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.put("/bookstore/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cartId\":1,\"bookId\":1,\"quantity\":1}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.header.successful").value(true));

        verify(bookCartGuestService, times(1)).updateBookCart(anyLong(), anyLong(), anyInt());
    }

    @Test
	void testUpdateBookCartMember() throws Exception {
		when(bookCartMemberService.updateBookCartMember(any(UpdateBookCartRequest.class)))
			.thenReturn(1L);

		mockMvc.perform(MockMvcRequestBuilders.put("/bookstore/carts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateBookCartRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.data").value(1L));
	}
    @Test
    void testDeleteBookCartGuest() throws Exception {
        when(bookCartGuestService.deleteBookCart(anyLong(),anyLong())).thenReturn((1L));

        mockMvc.perform(MockMvcRequestBuilders.delete("/bookstore/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cartId\":1,\"bookCartId\":1}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.header.resultCode").value(204))
                .andExpect(jsonPath("$.header.successful").value(true));
    }

	@Test
	void testDeleteBookCartMember() throws Exception {
		when(bookCartMemberService.deleteBookCartMember(any(DeleteBookCartRequest.class))).thenReturn(anyLong());

		mockMvc.perform(MockMvcRequestBuilders.delete("/bookstore/carts")
                .content(objectMapper.writeValueAsString(deleteBookCartRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Member-Id", "1"))
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.header.resultCode").value(204))
			.andExpect(jsonPath("$.header.successful").value(true));
	}


}