package com.nhnacademy.bookstore.member.member.controller;


import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstore.entity.auth.Auth;
import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.entity.member.enums.Status;
import com.nhnacademy.bookstore.entity.pointRecord.PointRecord;
import com.nhnacademy.bookstore.member.auth.dto.AuthResponse;
import com.nhnacademy.bookstore.member.auth.service.impl.AuthServiceImpl;
import com.nhnacademy.bookstore.member.member.dto.request.CreateMemberRequest;

import com.nhnacademy.bookstore.member.member.dto.request.UpdateMemberRequest;
import com.nhnacademy.bookstore.member.member.dto.request.UserProfile;
import com.nhnacademy.bookstore.member.member.dto.response.GetMemberResponse;
import com.nhnacademy.bookstore.member.member.dto.response.MemberAuthResponse;
import com.nhnacademy.bookstore.member.member.dto.response.UpdateMemberResponse;
import com.nhnacademy.bookstore.member.member.service.impl.MemberServiceImpl;
import com.nhnacademy.bookstore.member.memberAuth.service.impl.MemberAuthServiceImpl;
import com.nhnacademy.bookstore.member.pointRecord.service.impl.PointRecordServiceImpl;
import com.nhnacademy.bookstore.util.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * The type Member controller.
 *
 * @author 오연수, 유지아
 */
@RestController
@RequiredArgsConstructor
public class MemberController {
	private final MemberServiceImpl memberService;
	private final PointRecordServiceImpl pointRecordService;
	private final AuthServiceImpl authService;
	private final MemberAuthServiceImpl memberAuthService;
	private final PasswordEncoder passwordEncoder;

	/**
	 * Create member response entity.- 회원가입에 사용되는 함수이다.
	 *
	 * @param request the request - creatememberrequest를 받아 member를 생성한다.
	 * @author 유지아
	 */
	@Transactional
	@PostMapping("/bookstore/members")
	public ApiResponse<Void> createMember(@RequestBody @Valid CreateMemberRequest request) {
		CreateMemberRequest encodedRequest = new CreateMemberRequest(request.email(),
			passwordEncoder.encode(request.password()), request.name(), request.phone(), request.age(),
			request.birthday());
		Member member = new Member(encodedRequest);
		Auth auth = authService.getAuth("USER");
		memberService.save(member);
		PointRecord pointRecord = new PointRecord(null, 5000L, 5000L, ZonedDateTime.now(), "회원가입 5000포인트 적립.", member);
		pointRecordService.save(pointRecord);
		memberAuthService.saveAuth(member, auth);

		return new ApiResponse<Void>(new ApiResponse.Header(true, 201), new ApiResponse.Body<Void>(null));
	}

	/**
	 * Find by member id response entity. -멤버아이디를 기반으로 멤버정보를 가져온다.
	 *
	 * @return the response entity -멤버 정보에 대한 응답을 담아서 apiresponse로 응답한다.
	 * @author 유지아
	 */
	@Transactional
	@GetMapping("/bookstore/members")
	public ApiResponse<GetMemberResponse> readById(@RequestHeader("member-id") Long memberId) {

		Member member = memberService.readById(memberId);
		GetMemberResponse getMemberResponse = GetMemberResponse.builder()
			.age(member.getAge())
			.grade(member.getGrade())
			.point(member.getPoint())
			.phone(member.getPhone())
			.createdAt(member.getCreatedAt())
			.birthday(member.getBirthday())
			.email(member.getEmail())
			.name(member.getName())
			.password(member.getPassword()).build();
		return new ApiResponse<GetMemberResponse>(new ApiResponse.Header(true, 200),
			new ApiResponse.Body<>(getMemberResponse));

	}

	/**
	//  * Find by email and password response entity. -이메일과 비밀번호에 맞는 멤버정보를 반환한다.
	//  *
	//  * @param
	//  * @return the response entity -멤버 정보에 대한 응답을 담아서 apiresponse로 응답한다.
	//  * @author 유지아
	//  */
	// @PostMapping("/bookstore/members/login")
	// public ApiResponse<GetMemberResponse> readByEmailAndPassword(
	// 	@RequestBody @Valid LoginRequest loginRequest) {
	// 	Member member = memberService.readByEmailAndPassword(loginRequest.email(), loginRequest.password());
	// 	GetMemberResponse getMemberResponse = GetMemberResponse.builder()
	// 		.age(member.getAge())
	// 		.grade(member.getGrade())
	// 		.point(member.getPoint())
	// 		.phone(member.getPhone())
	// 		.createdAt(member.getCreatedAt())
	// 		.birthday(member.getBirthday())
	// 		.email(member.getEmail())
	// 		.name(member.getName())
	// 		.password(member.getPassword()).build();
	// 	memberService.updateStatus(member.getId(), Status.Active);//응답이 만들어 졌다는거는 로그인성공이란 소리니까 멤버를 업데이트 해야할 것같다..
	// 	memberService.updateLastLogin(member.getId(), ZonedDateTime.now());
	//
	// 	return new ApiResponse<GetMemberResponse>(new ApiResponse.Header(true, 200),
	// 		new ApiResponse.Body<>(getMemberResponse));
	//
	// }

	/**
	 * Find auths list. -권한에 대한 리스트를 받아온다.
	 *
	 * @param memberId the member id -멤버아이디를 받는다.
	 * @return the list -해당 유저에 대한 권한들을 응답에 담아 apiresponse로 응답한다.
	 * @author 유지아
	 */
	@Transactional
	@GetMapping("/bookstore/members/auths")
	public ApiResponse<List<AuthResponse>> readAuths(@RequestHeader("member-id") Long memberId) {

		return new ApiResponse<List<AuthResponse>>(new ApiResponse.Header(true, 200),
			new ApiResponse.Body<>(memberAuthService.readAllAuths(memberId)
				.stream()
				.map(a -> AuthResponse.builder().auth(a.getName()).build())
				.collect(
					Collectors.toList())));

	}

	/**
	 * 멤버 업데이트
	 *
	 * @param updateMemberRequest password, name, age, phone, email, birthday
	 * @return the api response - updateMemberResponse
	 * @author 오연수
	 */
	@Transactional
	@PutMapping("/bookstore/members")
	public ApiResponse<UpdateMemberResponse> updateMember(@RequestHeader(name = "Member-Id") Long memberId,
		@Valid @RequestBody UpdateMemberRequest updateMemberRequest) {

		Member updatedMember = memberService.updateMember(memberId, updateMemberRequest);
		UpdateMemberResponse updateMemberResponse = UpdateMemberResponse.builder()
			.id(String.valueOf(updatedMember.getId()))
			.name(updatedMember.getName()).build();
		return new ApiResponse<>(new ApiResponse.Header(true, 200), new ApiResponse.Body<>(updateMemberResponse));

	}

	/**
	 * 멤버 탈퇴 처리
	 *
	 * @param memberId 멤버 id
	 * @return the api response - Void
	 * @author 오연수
	 */
	@Transactional
	@DeleteMapping("/bookstore/members")
	public ApiResponse<Void> deleteMember(@RequestHeader(name = "Member-Id") Long memberId) {
		memberService.deleteMember(memberId);

		return new ApiResponse<>(new ApiResponse.Header(true, HttpStatus.NO_CONTENT.value()));

    }
	@PostMapping("/bookstore/members/oauth")
	public MemberAuthResponse oauthMember(@RequestBody @Valid UserProfile userProfile){
		//일단 무조건 auth는 general일꺼고..
		Auth auth = authService.getAuth("USER");
		Member member = memberService.saveOrGetPaycoMember(userProfile);
		PointRecord pointRecord = new PointRecord(null, 5000L, 5000L, ZonedDateTime.now(), "회원가입 5000포인트 적립.", member);
		pointRecordService.save(pointRecord);
		memberAuthService.saveAuth(member, auth);
		MemberAuthResponse memberAuthResponse = MemberAuthResponse.builder().email(member.getEmail()).memberId(member.getId()).auth(memberAuthService.readAllAuths(
			member.getId()).stream().map(a -> a + "").collect(Collectors.toList())).password(member.getPassword()).build();
		return memberAuthResponse;
	}
}

