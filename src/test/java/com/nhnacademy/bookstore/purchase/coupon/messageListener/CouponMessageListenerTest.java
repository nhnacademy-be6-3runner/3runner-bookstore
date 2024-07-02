package com.nhnacademy.bookstore.purchase.coupon.messageListener;

import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.entity.memberMessage.MemberMessage;
import com.nhnacademy.bookstore.member.member.exception.MemberNotExistsException;
import com.nhnacademy.bookstore.member.member.repository.MemberRepository;
import com.nhnacademy.bookstore.purchase.coupon.dto.CouponResponse;
import com.nhnacademy.bookstore.purchase.coupon.repository.CouponCustomRepository;
import com.nhnacademy.bookstore.purchase.memberMessage.dto.CouponFormDto;
import com.nhnacademy.bookstore.purchase.memberMessage.service.MemberMessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CouponMessageListenerTest {

    @Mock
    private CouponCustomRepository couponCustomRepository;

    @Mock
    private MemberMessageService memberMessageService;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CouponMessageListener couponMessageListener;


    @Test
    void testReceiveMessage() {
        CouponFormDto dto1 = new CouponFormDto(1L, ZonedDateTime.now(),ZonedDateTime.now(),ZonedDateTime.now(),"name", UUID.randomUUID(),1,2);
        CouponFormDto dto2 = new CouponFormDto(2L, ZonedDateTime.now(),ZonedDateTime.now(),ZonedDateTime.now(),"name", UUID.randomUUID(),2,4);
        List<CouponFormDto> couponFormDtos = Arrays.asList(dto1, dto2);

        CouponResponse response1 = new CouponResponse(1L, 1L);
        CouponResponse response2 = new CouponResponse(2L, 2L);
        List<CouponResponse> responses = Arrays.asList(response1, response2);
        Member member = new Member();

        when(couponCustomRepository.findMemberIdsByCouponFormIds(anyList())).thenReturn(responses);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberRepository.findById(2L)).thenReturn(Optional.of(member));

        couponMessageListener.receiveMessage(couponFormDtos);

        verify(couponCustomRepository, times(1)).findMemberIdsByCouponFormIds(anyList());
        verify(memberRepository, times(2)).findById(anyLong());
        verify(memberMessageService, times(2)).createMessage(any(MemberMessage.class));
    }

    @Test
    void testReceiveMessage_MemberNotExists() {
        CouponFormDto dto1 = new CouponFormDto(1L, ZonedDateTime.now(),ZonedDateTime.now(),ZonedDateTime.now(),"name", UUID.randomUUID(),1,2);
        List<CouponFormDto> couponFormDtos = Arrays.asList(dto1);

        CouponResponse response1 = new CouponResponse(1L, 1L);
        List<CouponResponse> responses = Arrays.asList(response1);

        when(couponCustomRepository.findMemberIdsByCouponFormIds(anyList())).thenReturn(responses);
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MemberNotExistsException.class, () -> {
            couponMessageListener.receiveMessage(couponFormDtos);
        });

        verify(couponCustomRepository, times(1)).findMemberIdsByCouponFormIds(anyList());
        verify(memberRepository, times(1)).findById(anyLong());
        verify(memberMessageService, times(0)).createMessage(any(MemberMessage.class));
    }
  
}