package com.nhnacademy.bookstore.member.member.service.impl;


import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.entity.member.enums.AuthProvider;
import com.nhnacademy.bookstore.entity.member.enums.Grade;
import com.nhnacademy.bookstore.entity.member.enums.Status;
import com.nhnacademy.bookstore.member.member.dto.request.CreateMemberRequest;
import com.nhnacademy.bookstore.member.member.dto.request.UpdateMemberRequest;
import com.nhnacademy.bookstore.member.member.dto.request.UserProfile;
import com.nhnacademy.bookstore.member.member.exception.AlreadyExistsEmailException;
import com.nhnacademy.bookstore.member.member.exception.LoginFailException;
import com.nhnacademy.bookstore.member.member.exception.LoginOauthEmailException;
import com.nhnacademy.bookstore.member.member.exception.MemberNotExistsException;
import com.nhnacademy.bookstore.member.member.repository.MemberRepository;
import com.nhnacademy.bookstore.member.member.service.MemberService;
import com.nhnacademy.bookstore.purchase.purchase.dto.response.ReadPurchaseResponse;
import com.nhnacademy.bookstore.purchase.purchase.repository.PurchaseRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The type Member service.
 *
 * @author 오연수, 유지아
 */
@Transactional
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
	private final MemberRepository memberRepository;
	private final PurchaseRepository purchaseRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public Member saveOrGetPaycoMember(UserProfile userProfile) {
        Optional<Member> optionalMember = memberRepository.findByEmail(userProfile.getEmail());
        if(optionalMember.isPresent()){
            return optionalMember.get();//존재하는경우, 그냥 멤버를 가져온다.
        }else{
            Member member = new Member();
            member.setEmail(userProfile.getEmail());
            member.setPassword(passwordEncoder.encode(userProfile.getId()));
            member.setGrade(Grade.General);
            member.setStatus(Status.Active);
            member.setName(userProfile.getName()!=null?userProfile.getName():"Payco");
            member.setPhone(userProfile.getMobile()!=null?userProfile.getMobile():"EmptyNumber");
            member.setPoint(5000L);
            member.setCreatedAt(ZonedDateTime.now());
			member.setLastLoginDate(ZonedDateTime.now());
			member.setAuthProvider(AuthProvider.PAYCO);
            memberRepository.save(member);
            return member;
        }
    }

    /**
     * Save member.
     *
     * @param request the member -Member값을 받아온다.
     * @return the member -저장 후 member값을 그대로 반환한다.
     * @author 유지아 Save member. -멤버값을 받아와 저장한다.(이메일 중복하는걸로 확인하면 좋을듯)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Member save(CreateMemberRequest request) {
        CreateMemberRequest encodedRequest = new CreateMemberRequest(request.email(),passwordEncoder.encode(request.password()),request.name(),request.phone(),request.age(),request.birthday());
        Member member = new Member(encodedRequest);

        return memberRepository.save(member);
    }

	/**
	 * Find by id member.
	 *
	 * @param id the id -long형인 memberid를 받는다.
	 * @return the member -member 반환
	 * @author 유지아 Find by id member. -memberid를 받아 멤버자체를 가져온다.
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Member readById(Long id) {
		Optional<Member> member = memberRepository.findById(id);
		if (member.isPresent()) {
			return member.get();
		} else {
			throw new MemberNotExistsException();
		}
	}



	public Member readByEmail(String email) {
		Optional<Member> member = memberRepository.findByEmail(email);
		if (member.isPresent()) {
			return member.get();
		}
		throw new LoginFailException();
	}

	/**
	 * 멤버 업데이트
	 *
	 * @param memberId            the member id
	 * @param updateMemberRequest password, name, age, phone, email, birthday
	 * @return the member
	 * @author 오연수
	 */
	public Member updateMember(Long memberId, UpdateMemberRequest updateMemberRequest) {
		Member member = memberRepository.findById(memberId).orElseThrow(MemberNotExistsException::new);

		member.setPassword(updateMemberRequest.password());
		member.setName(updateMemberRequest.name());
		member.setAge(updateMemberRequest.age());
		member.setEmail(updateMemberRequest.email());
		member.setPhone(updateMemberRequest.phone());
		member.setBirthday(updateMemberRequest.birthday());
		member.setModifiedAt(ZonedDateTime.now());

		return memberRepository.save(member);
	}

	/**
	 * 멤버 탈퇴
	 *
	 * @param memberId the member id
	 * @author 오연수
	 */
	public void deleteMember(Long memberId) {
		Member member = memberRepository.findById(memberId).orElseThrow(MemberNotExistsException::new);

		member.setStatus(Status.Withdrawn);
		member.setDeletedAt(ZonedDateTime.now());

		memberRepository.save(member);
	}

	/**
	 * Update member's status(활성, 탈퇴, 휴면).
	 *
	 * @param memberId the member id
	 * @param status   the status
	 * @return the member
	 * @author 오연수
	 */
	public Member updateStatus(Long memberId, Status status) {
		Member member = memberRepository.findById(memberId).orElseThrow(MemberNotExistsException::new);
		member.setStatus(status);
		member.setModifiedAt(ZonedDateTime.now());
		return memberRepository.save(member);
	}

	/**
	 * Update member's grade(general, royal, gold, platinum).
	 *
	 * @param memberId the member id
	 * @param grade    the grade
	 * @return the member
	 * @author 오연수
	 */
	public Member updateGrade(Long memberId, Grade grade) {
		Member member = memberRepository.findById(memberId).orElseThrow(MemberNotExistsException::new);
		member.setGrade(grade);
		member.setModifiedAt(ZonedDateTime.now());
		return memberRepository.save(member);
	}

	@Override
	public Member updateLastLogin(Long memberId, ZonedDateTime lastLogin) {
		Member member = memberRepository.findById(memberId).orElseThrow(MemberNotExistsException::new);
		member.setLastLoginDate(lastLogin);
		return memberRepository.save(member);
	}

	/**
	 * 주문 리스트 조회 멤버.
	 *
	 * @param memberId 맴버아이디
	 * @return 리스트
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<ReadPurchaseResponse> getPurchasesByMemberId(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(MemberNotExistsException::new);
		return purchaseRepository.findByMember(member)
			.stream()
			.map(purchase -> ReadPurchaseResponse.builder()
				.id(purchase.getId())
				.status(purchase.getStatus())
				.deliveryPrice(purchase.getDeliveryPrice())
				.totalPrice(purchase.getTotalPrice())
				.createdAt(purchase.getCreatedAt())
				.road(purchase.getRoad())
				.password(purchase.getPassword())
				.memberType(purchase.getMemberType())
				.build()
			).collect(Collectors.toList());
	}
}