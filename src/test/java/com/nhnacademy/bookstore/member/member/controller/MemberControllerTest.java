package com.nhnacademy.bookstore.member.member.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstore.entity.auth.Auth;
import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.entity.member.enums.Grade;
import com.nhnacademy.bookstore.entity.member.enums.Status;
import com.nhnacademy.bookstore.member.member.dto.request.CreateMemberRequest;
import com.nhnacademy.bookstore.member.member.dto.request.LoginRequest;
import com.nhnacademy.bookstore.member.member.dto.request.UpdateMemberRequest;
import com.nhnacademy.bookstore.member.member.dto.response.GetMemberResponse;
import com.nhnacademy.bookstore.member.member.service.impl.MemberServiceImpl;
import com.nhnacademy.bookstore.member.memberAuth.service.impl.MemberAuthServiceImpl;
import com.nhnacademy.bookstore.member.pointRecord.service.impl.PointServiceImpl;

@SpringBootTest
public class MemberControllerTest {

	MockMvc mockMvc;

	@MockBean
	MemberServiceImpl memberService;
	@MockBean
	PointServiceImpl pointService;
	@MockBean
	MemberAuthServiceImpl memberAuthService;

	@MockBean
	private Member member;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@BeforeEach
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.addFilter(new CharacterEncodingFilter("UTF-8", true))
			.alwaysDo(print())
			.build();
		member = new Member();
		member.setId(1L);
		member.setName("UpdatedName");
	}

	@Test
	@DisplayName("회원 가입 성공 테스트")
	void createMember_success() throws Exception {
		CreateMemberRequest createMemberRequest = CreateMemberRequest.builder()
			.age(10)
			.phone("01000000000")
			.email("abc@naver.com")
			.password("password")
			.name("abc")
			.birthday(ZonedDateTime.now())
			.build();
		Mockito.when(memberService.save(any())).thenReturn(member);
		mockMvc.perform(post("/bookstore/members")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createMemberRequest)))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("회원 정보 읽기 성공 테스트")
	void readById_success() throws Exception {
		Long memberId = 1L;
		Member member = new Member(); // 예제를 위해 필요한 멤버 필드들 설정
		member.setAge(30);
		member.setGrade(Grade.General);
		member.setPoint(100L);
		member.setPhone("01000000000");
		member.setCreatedAt(ZonedDateTime.now());
		member.setBirthday(ZonedDateTime.now());
		member.setEmail("abc@naver.com");
		member.setName("abc");
		member.setPassword("password");

		GetMemberResponse getMemberResponse = GetMemberResponse.builder()
			.age(member.getAge())
			.grade(member.getGrade())
			.point(member.getPoint())
			.phone(member.getPhone())
			.created_at(member.getCreatedAt())
			.birthday(member.getBirthday())
			.email(member.getEmail())
			.name(member.getName())
			.password(member.getPassword()).build();

		Mockito.when(memberService.readById(memberId)).thenReturn(member);

		mockMvc.perform(get("/members")
				.header("member-id", memberId))
			//.andDo(print())
			.andExpect(status().isOk());
		//.andExpect(jsonPath("$.body.age").value(member.getAge()));
		//.andExpect(jsonPath("$.body.grade").value(member.getGrade()))
		//.andExpect(jsonPath("$.body.point").value(member.getPoint()));
		//        ApiResponse<String> a = new ApiResponse<>(new ApiResponse.Header(true,200,"fawe"),new ApiResponse.Body<>("S"));
		//        a.

	}

	@Test
	@DisplayName("로그인 성공 테스트")
	void readByEmailAndPassword_success() throws Exception {
		LoginRequest loginRequest = new LoginRequest("abc@naver.com", "password");
		CreateMemberRequest createMemberRequest = new CreateMemberRequest("abc@naver.com", "password", "user",
			"01000000000", 10, null);
		Member member = new Member(createMemberRequest); // 멤버 초기화와 세부 설정
		member.setId(1L);

		Mockito.when(memberService.readByEmailAndPassword(loginRequest.email(), loginRequest.password()))
			.thenReturn(member);
		Mockito.when(memberService.updateStatus(Mockito.anyLong(), Mockito.eq(Status.Active))).thenReturn(member);
		Mockito.when(memberService.updateLastLogin(Mockito.anyLong(), Mockito.any(ZonedDateTime.class)))
			.thenReturn(member);
		mockMvc.perform(post("/bookstore/members/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.data.email").value(member.getEmail()));
	}

	@Test
	@DisplayName("권한 리스트 읽기 성공 테스트")
	void readAuths_success() throws Exception {
		Long memberId = 1L;
		Auth auth1 = new Auth();
		auth1.setName("ADMIN");
		Auth auth2 = new Auth();
		auth2.setName("USER");

		List<Auth> authList = Arrays.asList(auth1, auth2); // Auth 엔티티를 가정
		Mockito.when(memberAuthService.readAllAuths(memberId)).thenReturn(authList);

		mockMvc.perform(get("/bookstore/members/auths")
				.header("member-id", memberId))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.data[0].auth").value(authList.get(0).getName()))
			.andExpect(jsonPath("$.body.data[1].auth").value(authList.get(1).getName()));
	}

	@Test
	@DisplayName("멤버 업데이트 테스트")
	public void testMemberUpdate() throws Exception {
		UpdateMemberRequest updateMemberRequest = UpdateMemberRequest.builder()
			.password("newPassword")
			.name("newName")
			.age(25)
			.email("new@example.com")
			.phone("12345678900")
			.birthday(ZonedDateTime.parse("2000-01-01T00:00:00Z"))
			.build();
		Mockito.when(memberService.updateMember(anyLong(), any(UpdateMemberRequest.class))).thenReturn(member);

		mockMvc.perform(put("/bookstore/members")
				.header("Member-Id", "1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateMemberRequest)))
			.andExpect(status().isOk());

	}

	@Test
	@DisplayName("멤버 삭제(탈퇴) 테스트")
	public void testMemberDelete() throws Exception {
		Mockito.doNothing().when(memberService).deleteMember(anyLong());

		mockMvc.perform(MockMvcRequestBuilders.delete("/bookstore/members")
				.header("Member-Id", "1"))
			.andExpect(content().json("{\"header\":{\"resultCode\":204,\"successful\":true}}"));
	}

}
