package com.nhnacademy.bookstore.member.member.service.impl;


import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.entity.member.enums.Grade;
import com.nhnacademy.bookstore.entity.member.enums.Status;
import com.nhnacademy.bookstore.entity.purchase.Purchase;
import com.nhnacademy.bookstore.member.member.dto.request.UpdateMemberRequest;
import com.nhnacademy.bookstore.member.member.exception.MemberNotExistsException;
import com.nhnacademy.bookstore.member.member.repository.MemberRepository;
import com.nhnacademy.bookstore.member.member.service.MemberService;
import com.nhnacademy.bookstore.purchase.purchase.dto.response.ReadPurchaseResponse;
import com.nhnacademy.bookstore.purchase.purchase.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.List;
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

    /**
     * Save member.
     *
     * @param member the member -Member값을 받아온다.
     * @return the member -저장 후 member값을 그대로 반환한다.
     * @author 유지아 Save member. -멤버값을 받아와 저장한다.(이메일 중복하는걸로 확인하면 좋을듯)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Member save(Member member) {
        //이메일 중복 확인안해도 되려나,,
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
    public Member findById(Long id) {
        if(memberRepository.findById(id).isPresent()){
            return memberRepository.findById(id).get();
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found");
        }
    }

    /**
     * Find by email and password member.
     *
     * @param email    the email -string 이메일 값을 받는다.
     * @param password the password -string 비밀번호 값을 받는다.
     * @return the member -해당하는 member를 반환한다.
     * @author 유지아 Find by email and password member. -이메일과 패스워드 값으로 조회한다.
     */
    public Member findByEmailAndPassword(String email, String password) {
        if(memberRepository.findByEmailAndPassword(email,password) !=null){
            return memberRepository.findByEmailAndPassword(email,password);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found");
        }
    }

    /**
     * 멤버 업데이트
     *
     * @param memberId            the member id
     * @param updateMemberRequest password, name, age, phone, email, birthday
     * @return the member
     * @author 오연수
     */
    public Member updateMember(String memberId, UpdateMemberRequest updateMemberRequest) {
        Long id = Long.parseLong(memberId);
        Member member = memberRepository.findById(id).orElseThrow(MemberNotExistsException::new);

        member.setPassword(updateMemberRequest.password());
        member.setName(updateMemberRequest.name());
        member.setAge(updateMemberRequest.age());
        member.setEmail(updateMemberRequest.email());
        member.setPhone(updateMemberRequest.phone());
        member.setBirthday(updateMemberRequest.birthday());
        member.setModified_at(ZonedDateTime.now());

        return memberRepository.save(member);
    }

    /**
     * 멤버 탈퇴
     *
     * @param memberId the member id
     * @author 오연수
     */
    public void deleteMember(String memberId) {
        Long id = Long.parseLong(memberId);
        Member member = memberRepository.findById(id).orElseThrow(MemberNotExistsException::new);

        member.setStatus(Status.Withdrawn);
        member.setDeleted_at(ZonedDateTime.now());

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
    public Member updateStatus(String memberId, Status status) {
        Long id = Long.parseLong(memberId);
        Member member = memberRepository.findById(id).orElseThrow(MemberNotExistsException::new);
        member.setStatus(status);
        member.setModified_at(ZonedDateTime.now());
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
    public Member updateGrade(String memberId, Grade grade) {
        Long id = Long.parseLong(memberId);
        Member member = memberRepository.findById(id).orElseThrow(MemberNotExistsException::new);
        member.setGrade(grade);
        member.setModified_at(ZonedDateTime.now());
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