package com.nhnacademy.bookstore.entity.reviewLike;

import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.entity.review.Review;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class ReviewLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private ZonedDateTime createdAt;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Review review;
}
