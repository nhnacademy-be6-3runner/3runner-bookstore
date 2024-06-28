package com.nhnacademy.bookstore.entity.reviewImage;

import com.nhnacademy.bookstore.entity.review.Review;
import com.nhnacademy.bookstore.entity.totalImage.TotalImage;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class ReviewImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private Review review;

    @OneToOne(cascade = CascadeType.ALL)
    private TotalImage totalImage;

    public ReviewImage(Review review, TotalImage totalImage) {
        this.review = review;
        this.totalImage = totalImage;
    }
}
