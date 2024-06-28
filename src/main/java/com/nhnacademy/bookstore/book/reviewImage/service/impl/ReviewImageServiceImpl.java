package com.nhnacademy.bookstore.book.reviewImage.service.impl;

import com.nhnacademy.bookstore.book.image.exception.NotFindImageException;
import com.nhnacademy.bookstore.book.review.repository.ReviewRepository;
import com.nhnacademy.bookstore.book.reviewImage.dto.request.CreateReviewImageRequest;
import com.nhnacademy.bookstore.book.reviewImage.repository.ReviewImageRepository;
import com.nhnacademy.bookstore.book.reviewImage.service.ReviewImageService;
import com.nhnacademy.bookstore.entity.review.Review;
import com.nhnacademy.bookstore.entity.reviewImage.ReviewImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewImageServiceImpl implements ReviewImageService {

    private final ReviewImageRepository reviewImageRepository;
    private final ReviewRepository reviewRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public void createReviewImage(List<CreateReviewImageRequest> createReviewImageRequestList) {
        if (Objects.isNull(createReviewImageRequestList) || createReviewImageRequestList.isEmpty()) {
            return;
        }

        Optional<Review> review = reviewRepository.findById(createReviewImageRequestList.getFirst().reviewId());
        if (review.isEmpty()) {
            throw new NotFindImageException();
        }
        for (CreateReviewImageRequest createReviewImageRequest : createReviewImageRequestList) {
            ReviewImage reviewImage = new ReviewImage(
                    createReviewImageRequest.reviewId(),
                    createReviewImageRequest.url(),
                    review.get());
            reviewImageRepository.save(reviewImage);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public void createReviewImage(List<String> imageList, long reviewId) {
        List<CreateReviewImageRequest> createReviewImageRequestList = new ArrayList<>();
        for (String image : imageList) {
            createReviewImageRequestList.add(new CreateReviewImageRequest(image, reviewId));
        }
        createReviewImage(createReviewImageRequestList);
    }

    @Override
    public void createReviewImage(CreateReviewImageRequest createReviewImageRequest) {
        Optional<Review> review = reviewRepository.findById(createReviewImageRequest.reviewId());
        if (review.isEmpty()) {
            throw new NotFindImageException();
        }
        ReviewImage reviewImage = new ReviewImage(createReviewImageRequest.reviewId(),
                createReviewImageRequest.url(),
                review.get());
        reviewImageRepository.save(reviewImage);
    }
}
