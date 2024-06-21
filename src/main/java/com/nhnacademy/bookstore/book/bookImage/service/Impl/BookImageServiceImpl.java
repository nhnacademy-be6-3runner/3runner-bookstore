package com.nhnacademy.bookstore.book.bookImage.service.Impl;

import com.nhnacademy.bookstore.book.book.repository.BookRepository;
import com.nhnacademy.bookstore.book.bookImage.dto.request.CreateBookImageRequest;
import com.nhnacademy.bookstore.book.bookImage.repository.BookImageRepository;
import com.nhnacademy.bookstore.book.bookImage.service.BookImageService;
import com.nhnacademy.bookstore.book.image.exception.NotFindImageException;
import com.nhnacademy.bookstore.entity.book.Book;
import com.nhnacademy.bookstore.entity.bookImage.BookImage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookImageServiceImpl implements BookImageService {

    private final BookImageRepository bookImageRepository;
    private final BookRepository bookRepository;


    @Override
    public void createBookImage(CreateBookImageRequest bookImageRequest) {
        Optional<Book> book = bookRepository.findById(bookImageRequest.bookId());
//        if(Objects.isNull(book)){
//            throw new NotFindImageException();
//        }
        BookImage bookImageEntity = new BookImage(bookImageRequest.bookId(),
                bookImageRequest.url(),
                bookImageRequest.type(),
                null);
    }
}
