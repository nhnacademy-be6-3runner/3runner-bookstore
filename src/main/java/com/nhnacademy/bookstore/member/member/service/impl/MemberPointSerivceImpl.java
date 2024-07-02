package com.nhnacademy.bookstore.member.member.service.impl;

import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.member.member.exception.MemberNotExistsException;
import com.nhnacademy.bookstore.member.member.repository.MemberRepository;
import com.nhnacademy.bookstore.member.member.service.MemberPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 맴버 포인트 서비스 구현체.
 *
 * @author 김병우
 */
@Transactional
@Service
@RequiredArgsConstructor
public class MemberPointSerivceImpl implements MemberPointService {
    private final MemberRepository memberRepository;

    /**
     * 맴버 포인트 업데이트.
     *
     * @param memberId 맴버아이디
     * @param usePoint 사용포인트
     * @return 맴버아이디
     */
    @Override
    public Long updatePoint(Long memberId, Long usePoint) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotExistsException::new);
        Long currentPoint = member.getPoint();
        member.setPoint(currentPoint + usePoint);

        memberRepository.save(member);

        return memberId;
    }

}
