package com.nhnacademy.bookstore.purchase.refundRecord.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstore.entity.purchaseBook.PurchaseBook;
import com.nhnacademy.bookstore.entity.refund.Refund;
import com.nhnacademy.bookstore.entity.refund.enums.RefundStatus;
import com.nhnacademy.bookstore.entity.refundRecord.RefundRecord;
import com.nhnacademy.bookstore.purchase.purchaseBook.dto.response.ReadBookByPurchase;
import com.nhnacademy.bookstore.purchase.purchaseBook.dto.response.ReadPurchaseBookResponse;
import com.nhnacademy.bookstore.purchase.purchaseBook.exception.NotExistsPurchaseBook;
import com.nhnacademy.bookstore.purchase.purchaseBook.repository.PurchaseBookCustomRepository;
import com.nhnacademy.bookstore.purchase.purchaseBook.repository.PurchaseBookRepository;
import com.nhnacademy.bookstore.purchase.refund.exception.NotExistsRefund;
import com.nhnacademy.bookstore.purchase.refund.repository.RefundRepository;
import com.nhnacademy.bookstore.purchase.refundRecord.dto.response.ReadRefundRecordResponse;
import com.nhnacademy.bookstore.purchase.refundRecord.exception.AlreadyExistsRefundRecordRedis;
import com.nhnacademy.bookstore.purchase.refundRecord.exception.NotExistsRefundRecordRedis;
import com.nhnacademy.bookstore.purchase.refundRecord.repository.RefundRecordRedisRepository;
import com.nhnacademy.bookstore.purchase.refundRecord.repository.RefundRecordRepository;
import com.nhnacademy.bookstore.purchase.refundRecord.service.RefundRecordMemberService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RefundRecordMemberServiceImpl implements RefundRecordMemberService {

	private final RefundRecordRepository refundRecordRepository;
	private final RefundRecordRedisRepository refundRecordRedisRepository;
	private final PurchaseBookRepository purchaseBookRepository;
	private final RefundRepository refundRepository;
	private final PurchaseBookCustomRepository purchaseBookCustomRepository;

	@Override
	public Long createRefundRecordRedis(Long orderNumber, Long purchaseBookId, int price, int quantity,
		ReadBookByPurchase readBookByPurchase) {
		if (refundRecordRedisRepository.detailIsHit(orderNumber.toString(), purchaseBookId)) {
			throw new AlreadyExistsRefundRecordRedis();
		}
		ReadRefundRecordResponse readRefundRecordResponse = ReadRefundRecordResponse.builder()
			.readBookByPurchase(readBookByPurchase)
			.id(purchaseBookId)
			.price(price)
			.quantity(quantity)
			.build();
		return refundRecordRedisRepository.create(orderNumber.toString(), purchaseBookId, readRefundRecordResponse);
	}

	@Override
	public Long createRefundRecordAllRedis(Long orderNumber) {
		if (refundRecordRedisRepository.isHit(orderNumber.toString())) {
			throw new AlreadyExistsRefundRecordRedis();
		}
		List<ReadPurchaseBookResponse> responses = purchaseBookCustomRepository.readBookPurchaseResponses(orderNumber);
		for (ReadPurchaseBookResponse readPurchaseBookResponse : responses) {
			createRefundRecordRedis(orderNumber, readPurchaseBookResponse.id(),
				readPurchaseBookResponse.price(),
				readPurchaseBookResponse.quantity(), readPurchaseBookResponse.readBookByPurchase());
		}
		return orderNumber;
	}

	@Override
	public Long updateRefundRecordRedis(Long orderNumber, Long purchaseBookId, int quantity) {
		ReadPurchaseBookResponse purchaseBook = purchaseBookCustomRepository.readPurchaseBookResponse(
			purchaseBookId);
		if (!refundRecordRedisRepository.detailIsHit(orderNumber.toString(), purchaseBookId)) {

			return createRefundRecordRedis(orderNumber, purchaseBookId,
				(purchaseBook.price() / purchaseBook.quantity()) * quantity, quantity,
				purchaseBook.readBookByPurchase());

		}
		return refundRecordRedisRepository.update(orderNumber.toString(), purchaseBookId, quantity,
			purchaseBook.price() / purchaseBook.quantity());
	}

	@Override
	public Long updateRefundRecordAllRedis(Long orderNumber) {
		if (!refundRecordRedisRepository.isHit(orderNumber.toString())) {
			return createRefundRecordAllRedis(orderNumber);
		}

		List<ReadPurchaseBookResponse> responses = purchaseBookCustomRepository.readBookPurchaseResponses(orderNumber);
		for (ReadPurchaseBookResponse readPurchaseBookResponse : responses) {
			if (!refundRecordRedisRepository.detailIsHit(orderNumber.toString(), readPurchaseBookResponse.id())) {
				createRefundRecordRedis(orderNumber, readPurchaseBookResponse.id(),
					readPurchaseBookResponse.price(),
					readPurchaseBookResponse.quantity(), readPurchaseBookResponse.readBookByPurchase());
			} else {
				PurchaseBook purchaseBook = purchaseBookRepository.findById(readPurchaseBookResponse.id()).orElse(null);
				refundRecordRedisRepository.update(orderNumber.toString(), readPurchaseBookResponse.id(),
					readPurchaseBookResponse.quantity(), purchaseBook.getPrice() / purchaseBook.getQuantity());
			}
		}
		return orderNumber;

	}

	@Override
	public Long updateRefundRecordZeroAllRedis(Long orderNumber) {
		if (!refundRecordRedisRepository.isHit(orderNumber.toString())) {
			return createRefundRecordAllRedis(orderNumber);
		}
		List<ReadPurchaseBookResponse> responses = purchaseBookCustomRepository.readBookPurchaseResponses(orderNumber);
		for (ReadPurchaseBookResponse readPurchaseBookResponse : responses) {
			if (!refundRecordRedisRepository.detailIsHit(orderNumber.toString(), readPurchaseBookResponse.id())) {
				createRefundRecordRedis(orderNumber, readPurchaseBookResponse.id(),
					0,
					0, readPurchaseBookResponse.readBookByPurchase());
			} else {
				refundRecordRedisRepository.update(orderNumber.toString(), readPurchaseBookResponse.id(),
					0, 0);
			}
		}
		return orderNumber;
	}

	@Override
	public Long deleteRefundRecordRedis(Long memberId, Long purchaseBookId) {
		if (!refundRecordRedisRepository.isHit(memberId.toString())) {
			throw new NotExistsRefundRecordRedis();
		}
		return refundRecordRedisRepository.delete(memberId.toString(), purchaseBookId);
	}

	@Override
	public List<ReadRefundRecordResponse> readRefundRecordRedis(Long orderNumber) {
		if (!refundRecordRedisRepository.isHit(orderNumber.toString())) {
			throw new NotExistsRefundRecordRedis();
		}

		return refundRecordRedisRepository.readAll(orderNumber.toString());
	}

	@Override
	public Boolean createRefundRecord(Long orderNumber, Long refundId) {
		if (!refundRecordRedisRepository.isHit(orderNumber.toString())) {
			return false;
		}
		Refund refund = refundRepository.findById(refundId).orElseThrow(NotExistsRefund::new);
		if (!refund.getRefundRecordList().isEmpty()) {
			return false;
		}
		List<ReadRefundRecordResponse> readRefundRecordResponseList = readRefundRecordRedis(orderNumber);

		for (ReadRefundRecordResponse readRefundRecordResponse : readRefundRecordResponseList) {
			RefundRecord refundRecord = new RefundRecord();
			refundRecord.setRefund(refund);
			refundRecord.setPurchaseBook(purchaseBookRepository.findById(readRefundRecordResponse.id()).orElseThrow(
				NotExistsPurchaseBook::new));
			refundRecord.setPrice(readRefundRecordResponse.price());
			refundRecord.setQuantity(readRefundRecordResponse.quantity());
			refundRecordRepository.save(refundRecord);
		}
		refundRecordRedisRepository.deleteAll(orderNumber.toString());

		return true;

	}

}
