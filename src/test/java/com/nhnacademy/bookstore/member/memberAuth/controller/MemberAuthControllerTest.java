package com.nhnacademy.bookstore.member.memberAuth.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.nhnacademy.bookstore.entity.auth.Auth;
import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.member.member.service.MemberService;
import com.nhnacademy.bookstore.member.memberAuth.service.MemberAuthService;

/**
 * MemberAuthController 에 대한 테스트입니다.
 *
 * @author 오연수
 */
public class MemberAuthControllerTest {
	@Mock
	private MemberAuthService memberAuthService;

	@Mock
	private MemberService memberService;

	@InjectMocks
	private MemberAuthController memberAuthController;

	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(memberAuthController).build();
	}

	@DisplayName("존재하는 멤버 이메일을 통해 MemberAuth 를 가져오는 경우")
	@Test
	void testGetMemberAuth_Success() throws Exception {
		String email = "test@example.com";
		String password = "password";
		Long memberId = 1L;

		Member member = new Member();
		member.setId(memberId);
		member.setEmail(email);
		member.setPassword(password);

		Auth auth1 = new Auth();
		auth1.setName("USER");
		Auth auth2 = new Auth();
		auth2.setName("ADMIN");

		List<Auth> authList = List.of(auth1, auth2);

		when(memberService.readByEmail(anyString())).thenReturn(member);
		when(memberAuthService.readAllAuths(memberId)).thenReturn(authList);

		mockMvc.perform(post("/bookstore/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"email\":\"" + email + "\"}"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.header.resultCode").value(200))
			.andExpect(jsonPath("$.header.successful").value(true))
			.andExpect(jsonPath("$.body.data.email").value(email))
			.andExpect(jsonPath("$.body.data.password").value(password))
			.andExpect(jsonPath("$.body.data.auth").isArray())
			.andExpect(jsonPath("$.body.data.auth[0]").value("USER"))
			.andExpect(jsonPath("$.body.data.memberId").value(memberId));
	}

	@DisplayName("없는 멤버 이메일을 통해 MemberAuth 를 가져오는 경우")
	@Test
	void testGetMemberAuth_MemberNotFound() throws Exception {
		when(memberService.readByEmail(anyString())).thenReturn(null);

		mockMvc.perform(post("/bookstore/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"email\":\"unknown@example.com\"}"))
			.andExpect(status().isOk())
			.andExpect(content().string(""));
	}
}
