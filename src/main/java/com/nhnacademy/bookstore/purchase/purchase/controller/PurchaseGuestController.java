package com.nhnacademy.bookstore.purchase.purchase.controller;

import com.nhnacademy.bookstore.purchase.purchase.dto.request.CreatePurchaseRequest;
import com.nhnacademy.bookstore.purchase.purchase.dto.request.ReadDeletePurchaseGuestRequest;
import com.nhnacademy.bookstore.purchase.purchase.dto.request.UpdatePurchaseGuestRequest;
import com.nhnacademy.bookstore.purchase.purchase.dto.response.ReadPurchaseResponse;
import com.nhnacademy.bookstore.purchase.purchase.exception.PurchaseFormArgumentErrorException;
import com.nhnacademy.bookstore.purchase.purchase.service.PurchaseGuestService;
import com.nhnacademy.bookstore.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * 비회원 주문 컨트롤러.
 *
 * @author 김병우
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookstore")
public class PurchaseGuestController {
    private final PurchaseGuestService purchaseGuestService;

    /**
     * 비회원 주문 읽기.
     *
     * @param readPurchaseRequest 주문 폼
     * @param bindingResult validator
     * @return ReadPurchaseResponse
     */
    @GetMapping("/guests/purchases")
    public ApiResponse<ReadPurchaseResponse> readPurchase (@Valid @RequestBody ReadDeletePurchaseGuestRequest readPurchaseRequest,
                                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new PurchaseFormArgumentErrorException(bindingResult.getFieldErrors().toString());
        }

        ReadPurchaseResponse response = purchaseGuestService.readPurchase(readPurchaseRequest.orderNumber(), readPurchaseRequest.password());

        return new ApiResponse<ReadPurchaseResponse>(
                new ApiResponse.Header(true, 200),
                new ApiResponse.Body<>(response)
        );
    }

    /**
     * 비회원 주문 등록.
     *
     * @param createPurchaseRequest 맴버 등록 폼(password 추가)
     * @param bindingResult 오류검증
     * @return ApiResponse
     */
    @PostMapping("/guests/purchases")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> createPurchase (@Valid @RequestBody CreatePurchaseRequest createPurchaseRequest,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new PurchaseFormArgumentErrorException(bindingResult.getFieldErrors().toString());
        }

        //TODO : 폼에 비밀번호 넣을 수 있도록 뷰에서 따로 설정
        purchaseGuestService.createPurchase(createPurchaseRequest);

        return new ApiResponse<Void>(new ApiResponse.Header(true, 201));
    }

    /**
     * 비회원주문 상태 변경.
     *
     * @param updatePurchaseGuestRequest 비회원 상태 수정 폼
     * @param bindingResult Validator
     * @return ApiResponse
     */
    @PutMapping("/guests/purchases")
    public ApiResponse<Void> updatePurchaseStatus (@Valid @RequestBody UpdatePurchaseGuestRequest updatePurchaseGuestRequest,
                                                   BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throw new PurchaseFormArgumentErrorException(bindingResult.getFieldErrors().toString());
        }
        purchaseGuestService.updatePurchase(updatePurchaseGuestRequest);

        return new ApiResponse<>(new ApiResponse.Header(true, 200));
    }

    /**
     * 비회원 주문 삭제.
     *
     * @param readDeletePurchaseGuestRequest 주문폼
     * @param bindingResult 오류검증
     * @return ApiResponse
     */
    @DeleteMapping("/guests/purchases")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> deletePurchases (@Valid @RequestBody ReadDeletePurchaseGuestRequest readDeletePurchaseGuestRequest,
                                              BindingResult bindingResult) {

        purchaseGuestService.deletePurchase(readDeletePurchaseGuestRequest.orderNumber(), readDeletePurchaseGuestRequest.password());

        return new ApiResponse<>(new ApiResponse.Header(true, 204));
    }
}
