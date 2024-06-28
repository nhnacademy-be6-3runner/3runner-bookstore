package com.nhnacademy.bookstore.member.member.dto.response;

import java.time.ZonedDateTime;

import com.nhnacademy.bookstore.entity.member.enums.Grade;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record GetMemberResponse(@NotNull @Size(min = 1, max = 50) String password,
								@NotNull Long point,
								@NotNull @Size(min = 1, max = 10) String name,
								int age,
								@NotNull @Size(min = 1, max = 11) String phone,
								@NotNull @Column(unique = true) String email,
								ZonedDateTime birthday,
								@NotNull Grade grade,
								ZonedDateTime lastLoginDate,
								@NotNull ZonedDateTime createdAt,
								ZonedDateTime modifiedAt) {
}
