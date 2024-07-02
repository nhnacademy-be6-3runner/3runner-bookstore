package com.nhnacademy.bookstore.member.member.service;

/**
 * 맴버포인트서비스 인터페이스.
 *
 * @author 김병우
 */
public interface MemberPointService {
    Long updatePoint(Long memberId, Long usePoint);
}
