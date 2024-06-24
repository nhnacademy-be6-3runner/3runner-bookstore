package com.nhnacademy.bookstore.member.memberAuth.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstore.entity.auth.Auth;
import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.member.member.service.MemberService;
import com.nhnacademy.bookstore.member.memberAuth.dto.request.MemberAuthRequest;
import com.nhnacademy.bookstore.member.memberAuth.dto.response.MemberAuthResponse;
import com.nhnacademy.bookstore.member.memberAuth.service.MemberAuthService;

/**
 * 멤버 권한 정보를 관리하는 컨트롤러
 *
 * @author 오연수
 */
@RestController()
public class MemberAuthController {
	private final MemberService memberService;
	private final MemberAuthService memberAuthService;

	public MemberAuthController(MemberService memberService, MemberAuthService memberAuthService) {
		this.memberService = memberService;
		this.memberAuthService = memberAuthService;
	}

	/**
	 * 이메일을 통해 로그인에 필요한 멤버 정보를 반환한다.
	 *
	 * @param memberAuthRequest email
	 * @return 이메일, 패스워드, 권한 리스트, 멤버 아이디
	 */
	@PostMapping("/bookstore/login")
	public MemberAuthResponse getMemberAuth(@RequestBody MemberAuthRequest memberAuthRequest) {
		String email = memberAuthRequest.email();

		Member member = memberService.readByEmail(memberAuthRequest.email());
		if (Objects.nonNull(member)) {
			List<Auth> authList = memberAuthService.readAllAuths(member.getId());
			List<String> authStrList = authList.stream().map(Auth::getName).toList();

			return new MemberAuthResponse(email, member.getPassword(), authStrList, member.getId());
		}
		return null;
	}
}
