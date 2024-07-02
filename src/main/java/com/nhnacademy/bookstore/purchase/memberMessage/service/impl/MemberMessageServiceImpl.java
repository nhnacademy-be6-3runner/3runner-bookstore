package com.nhnacademy.bookstore.purchase.memberMessage.service.impl;

import com.nhnacademy.bookstore.entity.memberMessage.MemberMessage;
import com.nhnacademy.bookstore.purchase.memberMessage.repository.MemberMessageRepository;
import com.nhnacademy.bookstore.purchase.memberMessage.service.MemberMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
}
