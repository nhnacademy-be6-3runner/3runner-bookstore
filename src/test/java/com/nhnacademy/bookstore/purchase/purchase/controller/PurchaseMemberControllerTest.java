package com.nhnacademy.bookstore.purchase.purchase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstore.entity.purchase.enums.MemberType;
import com.nhnacademy.bookstore.entity.purchase.enums.PurchaseStatus;
import com.nhnacademy.bookstore.member.member.service.impl.MemberServiceImpl;
import com.nhnacademy.bookstore.purchase.purchase.dto.request.CreatePurchaseRequest;
import com.nhnacademy.bookstore.purchase.purchase.dto.request.UpdatePurchaseMemberRequest;
import com.nhnacademy.bookstore.purchase.purchase.dto.response.ReadPurchaseResponse;
import com.nhnacademy.bookstore.purchase.purchase.service.impl.PurchaseMemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PurchaseMemberController.class)
class PurchaseMemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PurchaseMemberServiceImpl purchaseService;

    @MockBean
    private MemberServiceImpl memberService;

    @Autowired
    private ObjectMapper objectMapper;

    CreatePurchaseRequest createPurchaseRequest;
    UpdatePurchaseMemberRequest updatePurchaseRequest;
    ReadPurchaseResponse readPurchaseResponse;

    @BeforeEach
    void setUp() {
        createPurchaseRequest = CreatePurchaseRequest.builder().deliveryPrice(100).totalPrice(10).road("orad").build();
        updatePurchaseRequest = UpdatePurchaseMemberRequest.builder().purchaseStatus(PurchaseStatus.SHIPPED).build();
        readPurchaseResponse = ReadPurchaseResponse.builder().
                id(1L).
                status(PurchaseStatus.SHIPPED).
                deliveryPrice(11).
                totalPrice(1).
                createdAt(ZonedDateTime.now()).
                road("road").
                password("pass").
                memberType(MemberType.MEMBER).
                build();
    }

    @Test
    void readPurchase() throws Exception {
        when(purchaseService.readPurchase(anyLong(),anyLong())).thenReturn(readPurchaseResponse);

        ResultActions result = mockMvc.perform(get("/members/purchases/1")
                .header("Member-Id",1L)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.body.data.id").value(readPurchaseResponse.id()))
                .andExpect(jsonPath("$.body.data.status").value(readPurchaseResponse.status().toString()))
                .andExpect(jsonPath("$.body.data.deliveryPrice").value(readPurchaseResponse.deliveryPrice()))
                .andExpect(jsonPath("$.body.data.totalPrice").value(readPurchaseResponse.totalPrice()))
                .andExpect(jsonPath("$.body.data.road").value(readPurchaseResponse.road()));
    }

    @Test
    void readPurchases() throws Exception {
        when(memberService.getPurchasesByMemberId(anyLong())).thenReturn(java.util.List.of(readPurchaseResponse));

        ResultActions result = mockMvc.perform(get("/members/purchases")
                .header("Member-Id",1L)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }


    @Test
    void createPurchase() throws Exception{
        when(purchaseService.createPurchase(any(CreatePurchaseRequest.class), anyLong())).thenReturn(1L);

        ResultActions result = mockMvc.perform(post("/members/purchases")
                .header("Member-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createPurchaseRequest)));

        result.andExpect(status().isCreated());
    }

    @Test
    void updatePurchaseStatus() throws Exception{
        when(purchaseService.updatePurchase(any(UpdatePurchaseMemberRequest.class), anyLong(), anyLong())).thenReturn(1L);

        ResultActions result = mockMvc.perform(put("/members/purchases/1")
                .header("Member-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePurchaseRequest)));

        result.andExpect(status().isOk());
    }

    @Test
    void deletePurchases() throws Exception{
        doNothing().when(purchaseService).deletePurchase(anyLong(), anyLong());

        ResultActions result = mockMvc.perform(delete("/members/purchases/1")
                .header("Member-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
    }
}