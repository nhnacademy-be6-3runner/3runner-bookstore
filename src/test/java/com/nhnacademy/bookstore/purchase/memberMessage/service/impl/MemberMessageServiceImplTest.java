package com.nhnacademy.bookstore.purchase.memberMessage.service.impl;

import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.entity.memberMessage.MemberMessage;
import com.nhnacademy.bookstore.purchase.memberMessage.repository.MemberMessageRepository;
import com.nhnacademy.bookstore.purchase.memberMessage.service.MemberMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberMessageServiceImplTest {

    @Mock
    private MemberMessageRepository memberMessageRepository;

    @InjectMocks
    private MemberMessageServiceImpl memberMessageService;

    @Test
    void testCreateMessage() {
        Member member = new Member();
        MemberMessage memberMessage = new MemberMessage("dasdf",member);
        memberMessage.setId(1L);
        memberMessage.setMessage("Test Message");

        when(memberMessageRepository.save(any(MemberMessage.class))).thenReturn(memberMessage);

        Long createdId = memberMessageService.createMessage(memberMessage);

        assertNotNull(createdId);
        assertEquals(1L, createdId);
        verify(memberMessageRepository, times(1)).save(any(MemberMessage.class));
    }
}