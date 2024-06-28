package com.nhnacademy.bookstore.book.bookImage.service;


import com.nhnacademy.bookstore.book.bookImage.dto.request.CreateBookImageRequest;
import com.nhnacademy.bookstore.entity.bookImage.BookImage;
import com.nhnacademy.bookstore.entity.bookImage.enums.BookImageType;
import java.util.List;

public interface BookImageService {

    void createBookImage(List<String> imageList, long bookId, BookImageType bookImageType);
    void createBookImage(List<CreateBookImageRequest> bookImageRequestList);
    void createBookImage(CreateBookImageRequest bookImage);
}
