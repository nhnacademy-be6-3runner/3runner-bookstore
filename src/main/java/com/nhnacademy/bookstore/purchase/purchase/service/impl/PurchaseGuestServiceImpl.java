package com.nhnacademy.bookstore.purchase.purchase.service.impl;

import com.nhnacademy.bookstore.entity.purchase.Purchase;
import com.nhnacademy.bookstore.entity.purchase.enums.MemberType;
import com.nhnacademy.bookstore.entity.purchase.enums.PurchaseStatus;
import com.nhnacademy.bookstore.purchase.purchase.dto.request.CreatePurchaseRequest;
import com.nhnacademy.bookstore.purchase.purchase.dto.request.UpdatePurchaseGuestRequest;
import com.nhnacademy.bookstore.purchase.purchase.dto.response.ReadPurchaseResponse;
import com.nhnacademy.bookstore.purchase.purchase.exception.PurchaseAlreadyExistException;
import com.nhnacademy.bookstore.purchase.purchase.exception.PurchaseDoesNotExistException;
import com.nhnacademy.bookstore.purchase.purchase.exception.PurchaseNoAuthorizationException;
import com.nhnacademy.bookstore.purchase.purchase.repository.PurchaseRepository;
import com.nhnacademy.bookstore.purchase.purchase.service.PurchaseGuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * 비회원 주문 서비스.
 *
 * @author 김병우
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PurchaseGuestServiceImpl implements PurchaseGuestService {
    private final PurchaseRepository purchaseRepository;
    private final PasswordEncoder encoder;

    /**
     * 비회원 주문 생성
     *
     * @param createPurchaseRequest 생성 폼
     * @return 주문아이디
     */
    @Override
    public Long createPurchase(CreatePurchaseRequest createPurchaseRequest) {
        Purchase purchase = new Purchase(
                UUID.fromString(createPurchaseRequest.orderId()),
                PurchaseStatus.PROCESSING,
                createPurchaseRequest.deliveryPrice(),
                createPurchaseRequest.totalPrice(),
                ZonedDateTime.now(),
                createPurchaseRequest.road(),
                encoder.encode(createPurchaseRequest.password()),
                MemberType.NONMEMBER,
                null,
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
     * 비회원 주문 업데이트.
     *
     * @param updatePurchaseGuestRequest 업데이트 폼
     * @return 주문아이디
     */
    @Override
    public Long updatePurchase(UpdatePurchaseGuestRequest updatePurchaseGuestRequest) {
        Optional<Purchase> purchaseOptional = purchaseRepository.findPurchaseByOrderNumber(updatePurchaseGuestRequest.orderNumber());
        Purchase purchase = validateGuest(purchaseOptional, updatePurchaseGuestRequest.password());

        purchase.setStatus(updatePurchaseGuestRequest.purchaseStatus());

        purchaseRepository.save(purchase);

        return purchase.getId();
    }

    /**
     * 비회원 주문 조회
     *
     * @param orderNumber 주문번호
     * @param password 주문 비밀번호
     * @return 비회원 주문
     */
    @Override
    public ReadPurchaseResponse readPurchase(UUID orderNumber, String password) {
        Optional<Purchase> purchaseOptional = purchaseRepository.findPurchaseByOrderNumber(orderNumber);
        Purchase purchase = validateGuest(purchaseOptional,password);

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
     * 비회원 주문 삭제.
     *
     * @param orderNumber 주문번호
     * @param password 주문 비밀번호
     */
    @Override
    public void deletePurchase(UUID orderNumber, String password) {
        Optional<Purchase> purchaseOptional = purchaseRepository.findPurchaseByOrderNumber(orderNumber);
        Purchase purchase = validateGuest(purchaseOptional,password);
        purchaseRepository.delete(purchase);
    }

    /**
     * 비회원 주문 번호, 비밀번호 검증
     *
     * @param purchaseOptional 주문
     * @param password 비밀번호
     * @return 주문
     */
    private Purchase validateGuest(Optional<Purchase> purchaseOptional, String password){
        if(purchaseOptional.isEmpty()){
            throw new PurchaseDoesNotExistException("주문이 없습니다.");
        }

        Purchase purchase = purchaseOptional.get();
        if(!encoder.matches(password, purchase.getPassword())){
            throw new PurchaseNoAuthorizationException("권한이 없습니다.");
        }
        return purchase;
    }
}
