package com.nhnacademy.bookstore.purchase.bookCart.service;

import java.util.List;

import com.nhnacademy.bookstore.purchase.bookCart.dto.request.CreateBookCartRequest;
import com.nhnacademy.bookstore.purchase.bookCart.dto.request.DeleteBookCartRequest;
import com.nhnacademy.bookstore.purchase.bookCart.dto.request.ReadAllBookCartMemberRequest;
import com.nhnacademy.bookstore.purchase.bookCart.dto.request.UpdateBookCartRequest;
import com.nhnacademy.bookstore.purchase.bookCart.dto.response.ReadAllBookCartMemberResponse;

public interface BookCartMemberService {
	List<ReadAllBookCartMemberResponse> readAllCartMember(ReadAllBookCartMemberRequest readAllCartMemberRequest);
	Long createBookCartMember(CreateBookCartRequest createBookCartRequest);
	Long updateBookCartMember(UpdateBookCartRequest updateBookCartRequest);
	Long deleteBookCartMember(DeleteBookCartRequest deleteBookCartMemberRequest);

}
