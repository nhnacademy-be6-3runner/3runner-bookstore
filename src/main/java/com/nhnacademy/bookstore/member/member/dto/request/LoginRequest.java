package com.nhnacademy.bookstore.member.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record LoginRequest(@Email @NotNull @NotEmpty String email, @NotNull @NotEmpty String password) {

}