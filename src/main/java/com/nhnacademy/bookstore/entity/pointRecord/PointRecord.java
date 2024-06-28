package com.nhnacademy.bookstore.entity.pointRecord;

import com.nhnacademy.bookstore.entity.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PointRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long memberPoint;
    @NotNull
    private Long remnantPoint;
    @NotNull
    private ZonedDateTime createdAt;
    @NotNull
    @Size(min = 1, max = 100)
    private String content;

    @NotNull
    @ManyToOne
    private Member member;
}
