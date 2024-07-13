package com.nhnacademy.bookstore.purchase.memberMessage.controller;

import com.nhnacademy.bookstore.purchase.memberMessage.dto.ReadMemberMessageResponse;
import com.nhnacademy.bookstore.purchase.memberMessage.dto.UpdateMemberMessageRequest;
import com.nhnacademy.bookstore.purchase.memberMessage.service.MemberMessageService;
import com.nhnacademy.bookstore.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberMessageController {
    private final MemberMessageService memberMessageService;

    @GetMapping("/bookstore/messages")
    public ApiResponse<List<ReadMemberMessageResponse>> readAllById(@RequestHeader("Member-Id") Long memberId) {

        return ApiResponse.success(memberMessageService.readAll(memberId));
    }

    @GetMapping("/bookstore/messages/counts")
    public ApiResponse<Long> readUnreadedMessage(@RequestHeader("Member-Id") Long memberId) {
        return ApiResponse.success(memberMessageService.countUnreadMessage(memberId));
    }


    @PutMapping("/bookstore/messages")
    public ApiResponse<Void> updateMessage(@RequestBody UpdateMemberMessageRequest updateMemberMessageRequest) {
        memberMessageService.readAlarm(updateMemberMessageRequest.memberMessageId());
        return ApiResponse.success(null);
    }
}
