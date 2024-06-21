package com.nhnacademy.bookstore.book.bookImage.service;


import com.nhnacademy.bookstore.book.bookImage.dto.request.CreateBookImageRequest;
import com.nhnacademy.bookstore.entity.bookImage.BookImage;

public interface BookImageService {

    void createBookImage(CreateBookImageRequest bookImage);
}
