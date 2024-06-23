package com.nhnacademy.bookstore.book.book.controller;

import com.nhnacademy.bookstore.book.book.dto.response.ReadBookResponse;
import com.nhnacademy.bookstore.book.book.exception.CreateBookRequestFormException;
import com.nhnacademy.bookstore.book.book.service.BookService;
import com.nhnacademy.bookstore.entity.book.Book;
import com.nhnacademy.bookstore.book.book.dto.request.CreateBookRequest;
import com.nhnacademy.bookstore.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * 책 요청 컨트롤러.
 *
 * @author 김병우
 */
@Tag(name="책", description = "책 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookstore/books")
public class BookController {
    private final BookService bookService;

    /**
     * 책 등록 요청 처리.
     *
     * @param createBookRequest request form
     * @param bindingResult binding result
     * @return ApiResponse<>
     */
    @PostMapping
    @Operation(summary = "등록", description = "책 등록")
    public ApiResponse<Void> createBook(@Valid @RequestBody CreateBookRequest createBookRequest,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CreateBookRequestFormException(bindingResult.getFieldErrors().toString());
        }

        bookService.createBook(createBookRequest);
        //TODO 북 카테고리 서비스로 추가
        //TODO 북 태그 서비스로 추가
        //TODO 북 이미지 서비스로 추가

        return new ApiResponse<Void>(new ApiResponse.Header(true, 201));
    }

    @GetMapping("/{bookId}")
    public ApiResponse<ReadBookResponse> readBook(@PathVariable("bookId") Long bookId) {
        ReadBookResponse book = bookService.readBookById(bookId);

        return new ApiResponse<ReadBookResponse>(
                new ApiResponse.Header(true, 200),
                new ApiResponse.Body<ReadBookResponse>(book)
        );
    }
}

