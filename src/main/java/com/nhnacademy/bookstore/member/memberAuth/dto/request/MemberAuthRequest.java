package com.nhnacademy.bookstore.member.memberAuth.dto.request;

import lombok.Builder;

@Builder
public record MemberAuthRequest(
	String email
) {
}

