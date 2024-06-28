package com.nhnacademy.bookstore.member.memberAuth.service;

import com.nhnacademy.bookstore.entity.auth.Auth;
import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.entity.memberAuth.MemberAuth;

import java.util.List;

public interface MemberAuthService {
    public List<Auth> readAllAuths(Long memberId);
    public void saveAuth(Member member, Auth auth);

}
