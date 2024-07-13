package com.nhnacademy.bookstore.purchase.memberMessage.service;

import com.nhnacademy.bookstore.entity.memberMessage.MemberMessage;
import com.nhnacademy.bookstore.purchase.memberMessage.dto.ReadMemberMessageResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MemberMessageService {
    Long createMessage(MemberMessage memberMessage);
    Page<ReadMemberMessageResponse> readAll(Long memberId, int page, int size);
    Long countUnreadMessage(Long memberId);
    void readAlarm(Long memberMessageId);
}
