package com.nhnacademy.bookstore.purchase.purchase.service.impl;

import com.nhnacademy.bookstore.entity.purchase.Purchase;
import com.nhnacademy.bookstore.entity.purchase.enums.MemberType;
import com.nhnacademy.bookstore.entity.purchase.enums.PurchaseStatus;
import com.nhnacademy.bookstore.member.member.service.MemberService;
import com.nhnacademy.bookstore.purchase.purchase.dto.request.CreatePurchaseRequest;
import com.nhnacademy.bookstore.purchase.purchase.dto.request.UpdatePurchaseMemberRequest;
import com.nhnacademy.bookstore.purchase.purchase.dto.response.ReadPurchaseResponse;
import com.nhnacademy.bookstore.purchase.purchase.exception.PurchaseAlreadyExistException;
import com.nhnacademy.bookstore.purchase.purchase.exception.PurchaseDoesNotExistException;
import com.nhnacademy.bookstore.purchase.purchase.exception.PurchaseNoAuthorizationException;
import com.nhnacademy.bookstore.purchase.purchase.repository.PurchaseRepository;
import com.nhnacademy.bookstore.purchase.purchase.service.PurchaseMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 회원 주문 서비스 구현체.
 *
 * @author 김병우
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseMemberServiceImpl implements PurchaseMemberService {
    private final PurchaseRepository purchaseRepository;
    private final MemberService memberService;

    /**
     * 주문 생성.
     *
     * @param createPurchaseRequest 주문 생성 폼
     * @param memberId 회원 아이디
     * @return purchaseId
     */
    @Override
    public Long createPurchase(CreatePurchaseRequest createPurchaseRequest, Long memberId) {
        Purchase purchase = new Purchase(
                UUID.randomUUID(),
                PurchaseStatus.PROCESSING,
                createPurchaseRequest.deliveryPrice(),
                createPurchaseRequest.totalPrice(),
                ZonedDateTime.now(),
                createPurchaseRequest.road(),
                null,
                MemberType.MEMBER,
                memberService.readById(memberId),
                null, //TODO : Point 구현 후 연결 필요
                null,
                null
        );

        if(purchaseRepository.existsPurchaseByOrderNumber(purchase.getOrderNumber())) {
            throw new PurchaseAlreadyExistException("주문 번호가 중복되었습니다.");
        }

        purchaseRepository.save(purchase);
        return purchase.getId();
    }

    /**
     * 주문 상태 업데이트.
     *
     * @param updatePurchaseRequest 주문수정폼
     * @param memberId 맴버아이디
     * @param purchaseId 주문아이디
     * @return purchaseId
     */
    @Override
    public Long updatePurchase(UpdatePurchaseMemberRequest updatePurchaseRequest, Long memberId, Long purchaseId) {
        List<Purchase> purchaseList = purchaseRepository.findByMember(memberService.readById(memberId));
        Purchase purchase = purchaseRepository.findById(purchaseId).orElseThrow(()-> new PurchaseDoesNotExistException(""));

        if (!purchaseList.contains(purchase)) {
            throw new PurchaseNoAuthorizationException("권한이 없습니다");
        }

        purchase.setStatus(updatePurchaseRequest.purchaseStatus());

        purchaseRepository.save(purchase);

        return purchase.getId();
    }


    /**
     * 회원 주문 찾기.
     *
     * @param MemberId 맴버 아이디
     * @param purchaseId 주문 아이디
     * @return ReadPurchaseResponse
     */
    @Override
    public ReadPurchaseResponse readPurchase(Long MemberId, Long purchaseId) {
        List<Purchase> purchaseList = purchaseRepository.findByMember(memberService.readById(MemberId));
        Purchase purchase = purchaseRepository.findById(purchaseId).orElseThrow(()-> new PurchaseDoesNotExistException(""));

        if (!purchaseList.contains(purchase)) {
            throw new PurchaseNoAuthorizationException("권한이 없습니다");
        }

        return ReadPurchaseResponse.builder()
                .id(purchase.getId())
                .status(purchase.getStatus())
                .deliveryPrice(purchase.getDeliveryPrice())
                .totalPrice(purchase.getTotalPrice())
                .createdAt(purchase.getCreatedAt())
                .road(purchase.getRoad())
                .password(purchase.getPassword())
                .memberType(purchase.getMemberType())
                .build();
    }

    /**
     * 회원 주문 삭제.
     *
     * @param MemberId 회원 아이디
     * @param purchaseId 주문 아이디
     */
    @Override
    public void deletePurchase(Long MemberId, Long purchaseId) {
        List<Purchase> purchaseList = purchaseRepository.findByMember(memberService.readById(MemberId));
        Purchase purchase = purchaseRepository.findById(purchaseId).orElseThrow(()-> new PurchaseDoesNotExistException(""));

        if (!purchaseList.contains(purchase)) {
            throw new PurchaseNoAuthorizationException("권한이 없습니다");
        }

        purchaseRepository.delete(purchase);
    }
}
