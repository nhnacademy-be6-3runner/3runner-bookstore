package com.nhnacademy.bookstore.purchase.purchase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstore.entity.purchase.enums.MemberType;
import com.nhnacademy.bookstore.entity.purchase.enums.PurchaseStatus;
import com.nhnacademy.bookstore.purchase.purchase.dto.request.CreatePurchaseRequest;
import com.nhnacademy.bookstore.purchase.purchase.dto.request.ReadDeletePurchaseGuestRequest;
import com.nhnacademy.bookstore.purchase.purchase.dto.request.UpdatePurchaseGuestRequest;
import com.nhnacademy.bookstore.purchase.purchase.dto.response.ReadPurchaseResponse;
import com.nhnacademy.bookstore.purchase.purchase.service.PurchaseGuestService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PurchaseGuestController.class)
class PurchaseGuestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PurchaseGuestService purchaseGuestService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreatePurchaseRequest createPurchaseRequest;
    private ReadDeletePurchaseGuestRequest readDeletePurchaseGuestRequest;
    private UpdatePurchaseGuestRequest updatePurchaseGuestRequest;
    private ReadPurchaseResponse readPurchaseResponse;

    @BeforeEach
    void setUp() {
        createPurchaseRequest = new CreatePurchaseRequest(100, 200, "123 Road", "password", UUID.randomUUID().toString());
        readDeletePurchaseGuestRequest = new ReadDeletePurchaseGuestRequest(UUID.randomUUID(), "password");
        updatePurchaseGuestRequest = new UpdatePurchaseGuestRequest(PurchaseStatus.SHIPPED, UUID.randomUUID(),"password");

        readPurchaseResponse = ReadPurchaseResponse.builder()
                .id(1L)
                .status(PurchaseStatus.PROCESSING)
                .deliveryPrice(100)
                .totalPrice(200)
                .createdAt(ZonedDateTime.now())
                .road("123 Road")
                .password("encodedPassword")
                .memberType(MemberType.NONMEMBER)
                .build();
    }
    @Test
    void readPurchase() throws Exception{
        Mockito.when(purchaseGuestService.readPurchase(any(UUID.class), anyString())).thenReturn(readPurchaseResponse);

        ResultActions result = mockMvc.perform(get("/bookstore/guests/purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(readDeletePurchaseGuestRequest)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.body.data.id").value(readPurchaseResponse.id()))
                .andExpect(jsonPath("$.body.data.status").value(readPurchaseResponse.status().toString()))
                .andExpect(jsonPath("$.body.data.deliveryPrice").value(readPurchaseResponse.deliveryPrice()))
                .andExpect(jsonPath("$.body.data.totalPrice").value(readPurchaseResponse.totalPrice()))
                .andExpect(jsonPath("$.body.data.road").value(readPurchaseResponse.road()));
    }

    @Test
    void createPurchase() throws Exception{
        Mockito.when(purchaseGuestService.createPurchase(any(CreatePurchaseRequest.class))).thenReturn(1L);

        ResultActions result = mockMvc.perform(post("/bookstore/guests/purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createPurchaseRequest)));

        result.andExpect(status().isCreated());
    }

    @Test
    void updatePurchaseStatus() throws Exception {
        Mockito.when(purchaseGuestService.updatePurchase(any(UpdatePurchaseGuestRequest.class))).thenReturn(1L);

        ResultActions result = mockMvc.perform(put("/bookstore/guests/purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePurchaseGuestRequest)));

        result.andExpect(status().isOk());
    }

    @Test
    void deletePurchases() throws Exception{
        Mockito.doNothing().when(purchaseGuestService).deletePurchase(any(UUID.class), anyString());

        ResultActions result = mockMvc.perform(delete("/bookstore/guests/purchases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(readDeletePurchaseGuestRequest)));

        result.andExpect(status().isNoContent());
    }
}