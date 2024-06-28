package com.nhnacademy.bookstore.purchase.purchase.repository;

import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.entity.purchase.Purchase;
import com.nhnacademy.bookstore.entity.purchase.enums.MemberType;
import com.nhnacademy.bookstore.entity.purchase.enums.PurchaseStatus;
import com.nhnacademy.bookstore.member.member.dto.request.CreateMemberRequest;
import com.nhnacademy.bookstore.member.member.repository.MemberRepository;
import com.nhnacademy.bookstore.member.member.service.MemberService;
import com.nhnacademy.bookstore.member.member.service.impl.MemberServiceImpl;
import com.nhnacademy.bookstore.purchase.purchase.dto.response.ReadPurchaseResponse;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class PurchaseRepositoryTest {
/*
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private MemberServiceImpl memberService;

    private Member member1;
    private Member member2;
    private Purchase purchase1;
    private Purchase purchase2;
    private Purchase purchase3;
    private Purchase purchase4;


    @BeforeEach
    void setUp() {
        member1 = new Member(CreateMemberRequest.builder().password("1").name("1").age(1).phone("1").birthday(ZonedDateTime.now()).email("dfdaf@nav.com").build());
        member2 = new Member(CreateMemberRequest.builder().password("1").name("1").age(1).phone("1").birthday(ZonedDateTime.now()).email("dfdadf@nav.com").build());

        memberService.save(member1);
        memberService.save(member2);

        purchase1 = new Purchase(UUID.randomUUID(), PurchaseStatus.SHIPPED, 100, 10, ZonedDateTime.now(), "road", "password", MemberType.MEMBER, member1,null,null,null);
        purchase1.setId(1L);
        purchase2 = new Purchase(UUID.randomUUID(), PurchaseStatus.SHIPPED, 100, 10, ZonedDateTime.now(), "road", "password", MemberType.MEMBER, member1,null,null,null);
        purchase2.setId(2L);
        purchase3 = new Purchase(UUID.randomUUID(), PurchaseStatus.SHIPPED, 100, 10, ZonedDateTime.now(), "road", "password", MemberType.MEMBER, member1,null,null,null);
        purchase3.setId(3L);
        purchase4 = new Purchase(UUID.randomUUID(), PurchaseStatus.SHIPPED, 100, 10, ZonedDateTime.now(), "road", "password", MemberType.MEMBER, member2,null,null,null);
        purchase4.setId(4L);

        purchaseRepository.save(purchase1);
        purchaseRepository.save(purchase2);
        purchaseRepository.save(purchase3);
        purchaseRepository.save(purchase4);

    }


    @AfterEach
    void tearDown() {
        memberService.deleteMember(member1.getId());
        memberService.deleteMember(member2.getId());
        purchaseRepository.delete(purchase1);
        purchaseRepository.delete(purchase2);
        purchaseRepository.delete(purchase3);
        purchaseRepository.delete(purchase4);
    }

    @Test
    void existsPurchaseByOrderNumber(){
        // assertTrue(purchaseRepository.existsPurchaseByOrderNumber(purchase1.getOrderNumber()));
        // assertTrue(purchaseRepository.existsPurchaseByOrderNumber(purchase2.getOrderNumber()));
        // assertTrue(purchaseRepository.existsPurchaseByOrderNumber(purchase3.getOrderNumber()));
        // assertFalse(purchaseRepository.existsPurchaseByOrderNumber(UUID.randomUUID()));
    }

    @Test
    void findPurchasesByMember(){
        // List<ReadPurchaseResponse> expectedList  = memberService.getPurchasesByMemberId(member1.getId());
//        List<ReadPurchaseResponse> actualList = List.of(purchase1, purchase2, purchase3);
//        assertEquals(expectedList, actualList);
    }
    */

}