package com.nhnacademy.bookstore.member.pointRecord.service.impl;

import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.entity.pointRecord.PointRecord;
import com.nhnacademy.bookstore.entity.purchase.Purchase;
import com.nhnacademy.bookstore.member.member.exception.MemberNotExistsException;
import com.nhnacademy.bookstore.member.member.repository.MemberRepository;
import com.nhnacademy.bookstore.member.member.service.MemberPointService;
import com.nhnacademy.bookstore.member.pointRecord.dto.response.ReadPointRecordResponse;
import com.nhnacademy.bookstore.member.pointRecord.exception.NoBuyPointRecordException;
import com.nhnacademy.bookstore.member.pointRecord.exception.NotEnoughPointException;
import com.nhnacademy.bookstore.member.pointRecord.repository.PointRecordRepository;
import com.nhnacademy.bookstore.purchase.purchase.exception.PurchaseDoesNotExistException;
import com.nhnacademy.bookstore.purchase.purchase.repository.PurchaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointRecordServiceImplTest {

    @Mock
    private PointRecordRepository pointRecordRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private MemberPointService memberPointService;

    @InjectMocks
    private PointRecordServiceImpl pointRecordService;

    @Test
    void testSave() {
//        Member member = new Member();
//        member.setId(1L);
//        member.setPoint(100L);
//
//        Purchase purchase = new Purchase();
//        purchase.setId(1L);
//
//        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
//        when(purchaseRepository.findById(anyLong())).thenReturn(Optional.of(purchase));
//
//        PointRecord pointRecord = new PointRecord(10L, "buy point", member, purchase);
//        when(pointRecordRepository.save(any(PointRecord.class))).thenReturn(pointRecord);
//
//        Long savedId = pointRecordService.save(10L, "buy point", 1L, 1L);
//
//        verify(pointRecordRepository, times(1)).save(any(PointRecord.class));
    }

    @Test
    void testSave_ThrowsNotEnoughPointException() {
        Member member = new Member();
        member.setId(1L);
        member.setPoint(0L);

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(purchaseRepository.findById(anyLong())).thenReturn(Optional.of(new Purchase()));

        assertThrows(NotEnoughPointException.class, () -> {
            pointRecordService.save(-10L, "buy point", 1L, 1L);
        });
    }

    @Test
    void testReadByMemberId() {
//        Member member = new Member();
//        member.setId(1L);
//
//        PointRecord pointRecord1 = new PointRecord();
//        pointRecord1.setId(1L);
//        pointRecord1.setUsePoint(10L);
//        pointRecord1.setCreatedAt(ZonedDateTime.now());
//        pointRecord1.setContent("Test1");
//
//        PointRecord pointRecord2 = new PointRecord();
//        pointRecord2.setId(2L);
//        pointRecord2.setUsePoint(20L);
//        pointRecord2.setCreatedAt(ZonedDateTime.now());
//        pointRecord2.setContent("Test2");
//
//        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
//        when(pointRecordRepository.findAllByMember(any(Member.class))).thenReturn(Arrays.asList(pointRecord1, pointRecord2));
//
//        List<ReadPointRecordResponse> responses = pointRecordService.readByMemberId(1L);
//
//        assertNotNull(responses);
//        assertEquals(2, responses.size());
//        assertEquals(10L, responses.get(0).usePoint());
//        assertEquals(20L, responses.get(1).usePoint());
    }

    @Test
    void testRefundByPurchaseId() {
//        Purchase purchase = new Purchase();
//        purchase.setId(1L);
//
//        PointRecord pointRecord = new PointRecord();
//        pointRecord.setId(1L);
//        pointRecord.setUsePoint(10L);
//        pointRecord.setContent("buy point");
//        pointRecord.setMember(new Member());
//        pointRecord.setPurchase(purchase);
//
//        when(purchaseRepository.findById(anyLong())).thenReturn(Optional.of(purchase));
//        when(pointRecordRepository.findAllByPurchase(any(Purchase.class))).thenReturn(Arrays.asList(pointRecord));
//        when(pointRecordRepository.save(any(PointRecord.class))).thenReturn(pointRecord);
//
//
//        verify(pointRecordRepository, times(1)).save(any(PointRecord.class));
    }

    @Test
    void testRefundByPurchaseId_NoBuyPointRecordException() {
        Purchase purchase = new Purchase();
        purchase.setId(1L);

        when(purchaseRepository.findById(anyLong())).thenReturn(Optional.of(purchase));
        when(pointRecordRepository.findAllByPurchase(any(Purchase.class))).thenReturn(Arrays.asList());

        assertThrows(NoBuyPointRecordException.class, () -> {
            pointRecordService.refundByPurchaseId(1L);
        });
    }
}