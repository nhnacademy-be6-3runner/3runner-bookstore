package com.nhnacademy.bookstore.purchase.purchaseBook.controller;

import com.nhnacademy.bookstore.purchase.purchaseBook.dto.request.CreatePurchaseBookRequest;
import com.nhnacademy.bookstore.purchase.purchaseBook.dto.request.DeletePurchaseBookRequest;
import com.nhnacademy.bookstore.purchase.purchaseBook.dto.request.ReadPurchaseIdRequest;
import com.nhnacademy.bookstore.purchase.purchaseBook.dto.request.UpdatePurchaseBookRequest;
import com.nhnacademy.bookstore.purchase.purchaseBook.dto.response.ReadPurchaseBookResponse;
import com.nhnacademy.bookstore.purchase.purchaseBook.exception.CreatePurchaseBookRequestFormException;
import com.nhnacademy.bookstore.purchase.purchaseBook.exception.DeletePurchaseBookRequestFormException;
import com.nhnacademy.bookstore.purchase.purchaseBook.exception.ReadPurchaseBookRequestFormException;
import com.nhnacademy.bookstore.purchase.purchaseBook.exception.UpdatePurchaseBookRequestFormException;
import com.nhnacademy.bookstore.purchase.purchaseBook.service.PurchaseBookService;
import com.nhnacademy.bookstore.util.ApiResponse;
import com.nhnacademy.bookstore.util.ValidationUtils;
import jakarta.validation.Valid;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 주문내역 - 책 controller
 *
 * @author 정주혁
 */
@RestController
@RequestMapping("/bookstore/purchase/book")
public class PurchaseBookController {

	private PurchaseBookService purchaseBookService;

	@Autowired
	public PurchaseBookController(PurchaseBookService purchaseBookService) {
		this.purchaseBookService = purchaseBookService;
	}

	/**
	 * 주문 id로 자신이 주문한 책들 조회
	 *
	 * @param readPurchaseIdRequest 주문아이디가 포함된 requestDto
	 * @param bindingResult requestDto의 오류발생시 오류 처리를 위한 파라미터
	 * @return 주문id로 조회된 책들 리스트 반환
	 */
	@GetMapping
	public ApiResponse<Page<ReadPurchaseBookResponse>> readPurchaseBook(
		@RequestBody @Valid ReadPurchaseIdRequest readPurchaseIdRequest, BindingResult bindingResult) {

		ValidationUtils.validateBindingResult(bindingResult, new ReadPurchaseBookRequestFormException(bindingResult));

		Pageable pageable;
		if (!Objects.isNull(readPurchaseIdRequest.sort())) {
			pageable = PageRequest.of(readPurchaseIdRequest.page(), readPurchaseIdRequest.page(),
				Sort.by(readPurchaseIdRequest.sort()));
		} else {
			pageable = PageRequest.of(readPurchaseIdRequest.page(), readPurchaseIdRequest.size());
		}
		Page<ReadPurchaseBookResponse> tmp =
			purchaseBookService.readBookByPurchaseResponses(readPurchaseIdRequest, pageable);
		return ApiResponse.success(tmp);
	}

	/**
	 * 주문 책 생성
	 *
	 * @param createPurchaseBookRequest 주문 책에 필요한 request dto
	 * @param bindingResult requestDto의 오류발생시 오류 처리를 위한 파라미터
	 * @return 생성한 주문 책 id
	 */
	@PostMapping
	public ApiResponse<Long> createPurchaseBook(@RequestBody @Valid CreatePurchaseBookRequest createPurchaseBookRequest,
		BindingResult bindingResult) {
		ValidationUtils.validateBindingResult(bindingResult, new CreatePurchaseBookRequestFormException(bindingResult));
		return ApiResponse.createSuccess(purchaseBookService.createPurchaseBook(createPurchaseBookRequest));
	}

	/**
	 * 주문 책 삭제
	 *
	 * @param deletePurchaseBookRequest 삭제할 주문 책의 id request dto
	 * @param bindingResult requestDto의 오류발생시 오류 처리를 위한 파라미터
	 * @return 삭제후 void return
	 */
	@DeleteMapping
	public ApiResponse<Void> deletePurchaseBook(@RequestBody @Valid DeletePurchaseBookRequest deletePurchaseBookRequest,
		BindingResult bindingResult) {
		ValidationUtils.validateBindingResult(bindingResult, new DeletePurchaseBookRequestFormException(bindingResult));
		purchaseBookService.deletePurchaseBook(deletePurchaseBookRequest);
		return ApiResponse.deleteSuccess(null);
	}

	/**
	 * 주문 책 수정
	 *
	 * @param updatePurchaseBookRequest 수정할 주문 책의 수정 내용
	 * @param bindingResult requestDto의 오류발생시 오류 처리를 위한 파라미터
	 * @return 수정한 주문책의 id
	 */
	@PutMapping
	public ApiResponse<Long> updatePurchaseBook(@RequestBody @Valid UpdatePurchaseBookRequest updatePurchaseBookRequest,
		BindingResult bindingResult) {
		ValidationUtils.validateBindingResult(bindingResult, new UpdatePurchaseBookRequestFormException(bindingResult));
		return ApiResponse.success(purchaseBookService.updatePurchaseBook(updatePurchaseBookRequest));
	}
}
