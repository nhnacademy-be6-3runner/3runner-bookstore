package com.nhnacademy.bookstore.member.member.dto.request;

import lombok.Builder;

import java.time.ZonedDateTime;

/**
 * The type Update member request.
 *
 * @author 오연수
 */
@Builder
public record UpdateMemberRequest (
    String password,
    String name,
    int age,
    String phone,
    String email,
    ZonedDateTime birthday
){}
