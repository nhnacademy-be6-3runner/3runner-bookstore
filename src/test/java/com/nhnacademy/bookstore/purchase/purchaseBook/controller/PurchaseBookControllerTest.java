package com.nhnacademy.bookstore.purchase.purchaseBook.controller;

import com.nhnacademy.bookstore.purchase.purchaseBook.dto.request.CreatePurchaseBookRequest;
import com.nhnacademy.bookstore.purchase.purchaseBook.dto.request.DeletePurchaseBookRequest;
import com.nhnacademy.bookstore.purchase.purchaseBook.dto.request.ReadPurchaseIdRequest;
import com.nhnacademy.bookstore.purchase.purchaseBook.dto.request.UpdatePurchaseBookRequest;
import com.nhnacademy.bookstore.purchase.purchaseBook.dto.response.ReadPurchaseBookResponse;
import com.nhnacademy.bookstore.purchase.purchaseBook.service.PurchaseBookService;
import com.nhnacademy.bookstore.purchase.purchaseBook.service.impl.PurchaseBookServiceImpl;
import com.nhnacademy.bookstore.util.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class PurchaseBookControllerTest {

    @Mock
    private PurchaseBookServiceImpl purchaseBookService;

    @InjectMocks
    private PurchaseBookController purchaseBookController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("주문-책 해당 주문id로 모두 조회")
    @Test
    void testReadPurchaseBook() {
        // Mock data
        ReadPurchaseIdRequest readPurchaseIdRequest = ReadPurchaseIdRequest.builder().purchaseId(1L).page(1).size(10).sort("Title").build();
        Page<ReadPurchaseBookResponse> mockResponse = new PageImpl<>(Collections.singletonList(
            ReadPurchaseBookResponse.builder().quantity(1).price(100).build()
        ));
        Pageable pageable = PageRequest.of(1, 10);

        // Mock service method
        when(purchaseBookService.readBookByPurchaseResponses(readPurchaseIdRequest,pageable)).thenReturn(mockResponse);

        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "readPurchaseIdRequest");

        // Call controller method
        ApiResponse<Page<ReadPurchaseBookResponse>> responseEntity = purchaseBookController.readPurchaseBook(readPurchaseIdRequest, bindingResult);

        // Verify
        assertEquals(200, responseEntity.getHeader().getResultCode());
        assertTrue(responseEntity.getHeader().isSuccessful());
        assertEquals(mockResponse, responseEntity.getBody().getData());

        verify(purchaseBookService).readBookByPurchaseResponses(readPurchaseIdRequest,pageable);
    }

    @DisplayName("주문-책 생성")
    @Test
    void testCreatePurchaseBook() {
        // Mock data
        CreatePurchaseBookRequest createPurchaseBookRequest = CreatePurchaseBookRequest.builder().bookId(1L).quantity(1).price(100).purchaseId(1L).build();
        Long expectedId = 1L;

        // Mock service method
        when(purchaseBookService.createPurchaseBook(any(CreatePurchaseBookRequest.class))).thenReturn(expectedId);

        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "createPurchaseBookRequest");

        // Call controller method
        ApiResponse<Long> responseEntity = purchaseBookController.createPurchaseBook(createPurchaseBookRequest, bindingResult);

        // Verify
        assertEquals(201, responseEntity.getHeader().getResultCode());
        assertTrue(responseEntity.getHeader().isSuccessful());
        assertEquals(expectedId, responseEntity.getBody().getData());

        verify(purchaseBookService).createPurchaseBook(createPurchaseBookRequest);
    }

    @DisplayName("주문-책 삭제")
    @Test
    void testDeletePurchaseBook() {
        // Mock data
        DeletePurchaseBookRequest deletePurchaseBookRequest = DeletePurchaseBookRequest.builder().purchaseBookId(1L).build();

        // Mock service method
        doNothing().when(purchaseBookService).deletePurchaseBook(any(DeletePurchaseBookRequest.class));
        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "deletePurchaseBookRequest");

        // Call controller method
        ApiResponse<Void> responseEntity = purchaseBookController.deletePurchaseBook(deletePurchaseBookRequest, bindingResult);

        // Verify
        assertEquals(204, responseEntity.getHeader().getResultCode());
        assertTrue(responseEntity.getHeader().isSuccessful());
        verify(purchaseBookService).deletePurchaseBook(deletePurchaseBookRequest);
    }

    @DisplayName("주문-책 수정")
    @Test
    void testUpdatePurchaseBook() {
        // Mock data
        UpdatePurchaseBookRequest updatePurchaseBookRequest = UpdatePurchaseBookRequest.builder().bookId(1L).quantity(1).price(100).purchaseId(1L).build();
        Long expectedId = 1L;

        // Mock service method
        when(purchaseBookService.updatePurchaseBook(any(UpdatePurchaseBookRequest.class))).thenReturn(expectedId);

        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "updatePurchaseBookRequest");

        // Call controller method
        ApiResponse<Long> responseEntity = purchaseBookController.updatePurchaseBook(updatePurchaseBookRequest, bindingResult);

        // Verify
        assertEquals(200, responseEntity.getHeader().getResultCode());
        assertTrue(responseEntity.getHeader().isSuccessful());
        assertEquals(expectedId, responseEntity.getBody().getData());

        verify(purchaseBookService).updatePurchaseBook(updatePurchaseBookRequest);
    }
}