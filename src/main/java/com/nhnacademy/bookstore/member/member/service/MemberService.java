package com.nhnacademy.bookstore.member.member.service;


import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.entity.member.enums.Grade;
import com.nhnacademy.bookstore.entity.member.enums.Status;
import com.nhnacademy.bookstore.entity.purchase.Purchase;
import com.nhnacademy.bookstore.member.member.dto.request.UpdateMemberRequest;
import com.nhnacademy.bookstore.purchase.purchase.dto.response.ReadPurchaseResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MemberService {
    Member save(Member member);

    Member findById(Long id);

    Member findByEmailAndPassword(String email, String password);

    Member updateMember(String memberId, UpdateMemberRequest updateMemberRequest);

    void deleteMember(String memberId);

    Member updateStatus(String memberId, Status status);

    Member updateGrade(String memberId, Grade grade);

    List<ReadPurchaseResponse> getPurchasesByMemberId(Long memberId);
}
