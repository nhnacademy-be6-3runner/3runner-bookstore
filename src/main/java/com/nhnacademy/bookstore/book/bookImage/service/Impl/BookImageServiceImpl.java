package com.nhnacademy.bookstore.book.bookImage.service.Impl;

import com.nhnacademy.bookstore.book.book.repository.BookRepository;
import com.nhnacademy.bookstore.book.bookImage.dto.request.CreateBookImageRequest;
import com.nhnacademy.bookstore.book.bookImage.repository.BookImageRepository;
import com.nhnacademy.bookstore.book.bookImage.service.BookImageService;
import com.nhnacademy.bookstore.book.image.exception.NotFindImageException;
import com.nhnacademy.bookstore.entity.book.Book;
import com.nhnacademy.bookstore.entity.bookImage.BookImage;
import com.nhnacademy.bookstore.entity.bookImage.enums.BookImageType;
import com.nhnacademy.bookstore.entity.totalImage.TotalImage;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookImageServiceImpl implements BookImageService {

    private final BookImageRepository bookImageRepository;
    private final BookRepository bookRepository;


    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public void createBookImage(List<String> imageList, long bookId, BookImageType bookImageType) {
        List<CreateBookImageRequest> createBookImageRequestList = new ArrayList<>();
        for(String image : imageList) {
            createBookImageRequestList.add(new CreateBookImageRequest(image, bookImageType, bookId));
        }
        createBookImage(createBookImageRequestList);
    }

    /**
     * List 로 받아와서 한꺼번에 추가
     * book Id 가 같기 떄문에 한번만 받아와 다 추가
     * @param bookImageRequestList 추가할 book 과 Image
     */
    @Override
    public void createBookImage(List<CreateBookImageRequest> bookImageRequestList) {
        if(Objects.isNull(bookImageRequestList) || bookImageRequestList.isEmpty()) {
            return ;
        }

        Optional<Book> book = bookRepository.findById(bookImageRequestList.getFirst().bookId());
        if(book.isEmpty()){
            throw new NotFindImageException();
        }
        for(CreateBookImageRequest bookImageRequest : bookImageRequestList) {

            TotalImage totalImage = new TotalImage(bookImageRequest.url());
            BookImage bookImageEntity = new BookImage(
                    bookImageRequest.type(),
                    book.get(),
                    totalImage
                );

            bookImageRepository.save(bookImageEntity);
        }
    }

    /**
     * Book Image 다대다 연결을 위한 함수
     * @param bookImageRequest  bookImageRequestDto
     */
    @Override
    public void createBookImage(CreateBookImageRequest bookImageRequest) {
        Optional<Book> book = bookRepository.findById(bookImageRequest.bookId());
        if(book.isEmpty()){
            throw new NotFindImageException();
        }
        TotalImage totalImage = new TotalImage(bookImageRequest.url());
        BookImage bookImageEntity = new BookImage(
            bookImageRequest.type(),
            book.get(),
            totalImage
        );

        bookImageRepository.save(bookImageEntity);
    }
}
