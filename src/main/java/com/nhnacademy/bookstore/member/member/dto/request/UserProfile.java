package com.nhnacademy.bookstore.member.member.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfile {
	private String idNo;
	private String email;
	private String mobile;
	private String name;

	// 생성자
	public UserProfile(String idNo, String email, String mobile, String name) {
		this.idNo = idNo;
		this.email = email;
		this.mobile = mobile;
		this.name = name;
	}
}
