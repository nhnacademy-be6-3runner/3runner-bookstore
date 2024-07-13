package com.nhnacademy.bookstore.purchase.memberMessage.service.impl;

import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.entity.memberMessage.MemberMessage;
import com.nhnacademy.bookstore.member.member.exception.MemberNotExistsException;
import com.nhnacademy.bookstore.member.member.repository.MemberRepository;
import com.nhnacademy.bookstore.purchase.memberMessage.dto.ReadMemberMessageResponse;
import com.nhnacademy.bookstore.purchase.memberMessage.exception.MemberMessageDoesNotExistException;
import com.nhnacademy.bookstore.purchase.memberMessage.repository.MemberMessageRepository;
import com.nhnacademy.bookstore.purchase.memberMessage.service.MemberMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * 맴버메시지 서비스 구현체.
 *
 * @author 김병우
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberMessageServiceImpl implements MemberMessageService {
    private final MemberMessageRepository memberMessageRepository;
    private final MemberRepository memberRepository;

    /**
     * 맴버메시지 생성.
     *
     * @param memberMessage 맴버메시지
     * @return 멤버메시지아이디
     */
    @Override
    public Long createMessage(MemberMessage memberMessage) {
        memberMessageRepository.save(memberMessage);
        return memberMessage.getId();
    }


    @Override
    public Page<ReadMemberMessageResponse> readAll(Long memberId, int page, int size) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotExistsException::new);

        Pageable pageable = PageRequest.of(page, size);

        return memberMessageRepository.findByMember(member, pageable)
                .map(m -> ReadMemberMessageResponse.builder()
                        .id(m.getId())
                        .message(m.getMessage())
                        .viewAt(m.getViewAt())
                        .sendAt(m.getSendAt())
                        .build()
                );
    }

    @Override
    public Long countUnreadMessage(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotExistsException::new);

        return memberMessageRepository.countByViewAtIsNull(member);
    }

    @Override
    public void readAlarm(Long memberMessageId) {
        MemberMessage memberMessage = memberMessageRepository
                .findById(memberMessageId)
                .orElseThrow(()->new MemberMessageDoesNotExistException(memberMessageId+"맴버메시지 아이디가 존재하지 않습니다."));

        memberMessage.setViewAt(ZonedDateTime.now());
        memberMessageRepository.save(memberMessage);
    }
}
