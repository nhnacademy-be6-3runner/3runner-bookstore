package com.nhnacademy.bookstore.book.reviewImage.service;

import com.nhnacademy.bookstore.book.reviewImage.dto.request.CreateReviewImageRequest;

import java.util.List;

public interface ReviewImageService {
    void createReviewImage(List<CreateReviewImageRequest> createReviewImageRequestList);

    void createReviewImage(List<String> imageList, long reviewId);

    void createReviewImage(CreateReviewImageRequest createReviewImageRequest);
}
