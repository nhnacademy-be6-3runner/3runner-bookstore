package com.nhnacademy.bookstore.member.memberAuth.service.impl;

import com.nhnacademy.bookstore.entity.auth.Auth;
import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.entity.memberAuth.MemberAuth;
import com.nhnacademy.bookstore.member.memberAuth.repository.MemberAuthRepository;
import com.nhnacademy.bookstore.member.memberAuth.service.MemberAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberAuthServiceImpl implements MemberAuthService {
    private final MemberAuthRepository memberAuthRepository;

    /**
     * @Author -유지아
     * Find all auths list. -멤버의 모든 권한을 리스트로 가져온다.
     *
     * @param memberId the member id -memberid값을 받는다.
     * @return the list -권한이 담긴 리스트를 반환한다.
     */
    public List<Auth> readAllAuths(Long memberId) {
        return memberAuthRepository.findByMemberId(memberId);
    }

    /**
     * @Author -유지아
     * Save auth. -유저의 권한을 저장한다.
     *
     * @param member the member -Member를 가져온다.
     * @param auth   the auth -Auth를 가져온다.
     */
    public void saveAuth(Member member, Auth auth){
        MemberAuth memberAuth = new MemberAuth();
        memberAuth.setMember(member);
        memberAuth.setAuth(auth);
        memberAuthRepository.save(memberAuth);
    }
}