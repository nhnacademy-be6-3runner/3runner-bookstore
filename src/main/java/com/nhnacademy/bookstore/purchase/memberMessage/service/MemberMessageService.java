package com.nhnacademy.bookstore.purchase.memberMessage.service;

import com.nhnacademy.bookstore.entity.memberMessage.MemberMessage;
import com.nhnacademy.bookstore.purchase.memberMessage.dto.ReadMemberMessageResponse;

import java.util.List;

public interface MemberMessageService {
    Long createMessage(MemberMessage memberMessage);
    List<ReadMemberMessageResponse> readAll(Long memberId);
    Long countUnreadMessage(Long memberId);
    void readAlarm(Long memberMessageId);
}
