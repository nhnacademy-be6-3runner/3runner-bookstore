package com.nhnacademy.bookstore.purchase.purchase.service.impl;

import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.entity.purchase.Purchase;
import com.nhnacademy.bookstore.entity.purchase.enums.MemberType;
import com.nhnacademy.bookstore.entity.purchase.enums.PurchaseStatus;
import com.nhnacademy.bookstore.member.member.dto.request.CreateMemberRequest;
import com.nhnacademy.bookstore.member.member.service.MemberService;
import com.nhnacademy.bookstore.purchase.purchase.dto.request.CreatePurchaseRequest;
import com.nhnacademy.bookstore.purchase.purchase.dto.request.UpdatePurchaseMemberRequest;
import com.nhnacademy.bookstore.purchase.purchase.dto.response.ReadPurchaseResponse;
import com.nhnacademy.bookstore.purchase.purchase.exception.PurchaseAlreadyExistException;
import com.nhnacademy.bookstore.purchase.purchase.exception.PurchaseNoAuthorizationException;
import com.nhnacademy.bookstore.purchase.purchase.repository.PurchaseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseMemberServiceImplTest {
    @Mock
    private PurchaseRepository purchaseRepository;
    @Mock
    private MemberService memberService;

    @InjectMocks
    private PurchaseMemberServiceImpl purchaseMemberService;

    private Member member1;
    private Purchase purchase1;
    private CreatePurchaseRequest request;

    @BeforeEach
    void setUp() {
        member1 = new Member(CreateMemberRequest.builder().password("1").name("1").age(1).phone("1").birthday(ZonedDateTime.now()).email("dfdaf@nav.com").build());
        member1.setId(1L);
        purchase1 = new Purchase(UUID.randomUUID(), PurchaseStatus.SHIPPED, 100, 10, ZonedDateTime.now(), "road", "password", MemberType.MEMBER, member1,null,null,null);

        request = CreatePurchaseRequest.builder().deliveryPrice(100).totalPrice(1000).road("dfdfd").build();
    }
    @AfterEach
    void tearDown(){
        memberService.deleteMember(member1.getId());
    }

    @Test
    void createPurchase() {
        when(memberService.readById(1L)).thenReturn(member1);
        when(purchaseRepository.existsPurchaseByOrderNumber(any(UUID.class))).thenReturn(false);
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(purchase1);

        Long purchaseId = purchaseMemberService.createPurchase(request, 1L);

        assertNotNull(purchaseId);
        verify(purchaseRepository, times(1)).save(any(Purchase.class));
    }

    @Test
    void createPurchase_existsOrderNumber() {
        when(memberService.readById(1L)).thenReturn(member1);
        when(purchaseRepository.existsPurchaseByOrderNumber(any(UUID.class))).thenReturn(true);

        assertThrows(PurchaseAlreadyExistException.class,()->{
            Long purchaseId = purchaseMemberService.createPurchase(request, 1L);
        });
    }

    @Test
    void updatePurchase() {
        when(memberService.readById(1L)).thenReturn(member1);
        when(purchaseRepository.findByMember(member1)).thenReturn(List.of(purchase1));
        when(purchaseRepository.findById(purchase1.getId())).thenReturn(Optional.of(purchase1));
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(purchase1);

        Long purchaseId = purchaseMemberService.updatePurchase(UpdatePurchaseMemberRequest.builder().purchaseStatus(PurchaseStatus.SHIPPED).build(),1L, purchase1.getId());

        assertNotNull(purchaseId);
        assertEquals(PurchaseStatus.SHIPPED, purchase1.getStatus());
        verify(purchaseRepository, times(1)).save(purchase1);
    }

    @Test
    void updatePurchase_AuthorizationThenThrowException() {
        when(memberService.readById(1L)).thenReturn(member1);
        when(purchaseRepository.findByMember(member1)).thenReturn(List.of());
        when(purchaseRepository.findById(purchase1.getId())).thenReturn(Optional.of(purchase1));

        assertThrows(PurchaseNoAuthorizationException.class,()->{
            Long purchaseId = purchaseMemberService.updatePurchase(UpdatePurchaseMemberRequest.builder().purchaseStatus(PurchaseStatus.SHIPPED).build(),1L, purchase1.getId());
        });
    }

    @Test
    void readPurchase() {
        when(memberService.readById(1L)).thenReturn(member1);
        when(purchaseRepository.findByMember(member1)).thenReturn(List.of(purchase1));
        when(purchaseRepository.findById(purchase1.getId())).thenReturn(Optional.of(purchase1));

        ReadPurchaseResponse response = purchaseMemberService.readPurchase(member1.getId(), purchase1.getId());
        assertNotNull(response);
        assertEquals(response.id(), purchase1.getId());
        verify(purchaseRepository, times(1)).findById(purchase1.getId());
    }


    @Test
    void readPurchase_AuthorizationThenThrowException() {
        when(memberService.readById(1L)).thenReturn(member1);
        when(purchaseRepository.findByMember(member1)).thenReturn(List.of());
        when(purchaseRepository.findById(purchase1.getId())).thenReturn(Optional.of(purchase1));

        assertThrows(PurchaseNoAuthorizationException.class,()->{
            purchaseMemberService.readPurchase(1L, purchase1.getId());
        });
    }

    @Test
    void deletePurchase() {
        when(memberService.readById(1L)).thenReturn(member1);
        when(purchaseRepository.findByMember(member1)).thenReturn(List.of(purchase1));
        when(purchaseRepository.findById(purchase1.getId())).thenReturn(Optional.of(purchase1));

        assertDoesNotThrow(()->{
            purchaseMemberService.deletePurchase(1L, purchase1.getId());
        });
    }

    @Test
    void deletePurchase_AuthorizationThenThrowException() {
        when(memberService.readById(1L)).thenReturn(member1);
        when(purchaseRepository.findByMember(member1)).thenReturn(List.of());
        when(purchaseRepository.findById(purchase1.getId())).thenReturn(Optional.of(purchase1));

        assertThrows(PurchaseNoAuthorizationException.class,()->{
            purchaseMemberService.deletePurchase(1L, purchase1.getId());
        });
    }
}