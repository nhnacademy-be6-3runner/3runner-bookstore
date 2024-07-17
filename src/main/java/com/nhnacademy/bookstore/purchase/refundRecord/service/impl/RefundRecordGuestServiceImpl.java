package com.nhnacademy.bookstore.purchase.refundRecord.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstore.entity.purchase.Purchase;
import com.nhnacademy.bookstore.entity.purchaseBook.PurchaseBook;
import com.nhnacademy.bookstore.entity.refund.Refund;
import com.nhnacademy.bookstore.entity.refundRecord.RefundRecord;
import com.nhnacademy.bookstore.purchase.purchase.repository.PurchaseRepository;
import com.nhnacademy.bookstore.purchase.purchaseBook.dto.response.ReadBookByPurchase;
import com.nhnacademy.bookstore.purchase.purchaseBook.dto.response.ReadPurchaseBookResponse;
import com.nhnacademy.bookstore.purchase.purchaseBook.exception.NotExistsPurchaseBook;
import com.nhnacademy.bookstore.purchase.purchaseBook.repository.PurchaseBookCustomRepository;
import com.nhnacademy.bookstore.purchase.purchaseBook.repository.PurchaseBookRepository;
import com.nhnacademy.bookstore.purchase.purchaseBook.repository.impl.PurchaseBookCustomRepositoryImpl;
import com.nhnacademy.bookstore.purchase.refund.exception.NotExistsRefund;
import com.nhnacademy.bookstore.purchase.refund.exception.NotExistsRefundRecord;
import com.nhnacademy.bookstore.purchase.refund.repository.RefundRepository;
import com.nhnacademy.bookstore.purchase.refundRecord.dto.response.ReadRefundRecordResponse;
import com.nhnacademy.bookstore.purchase.refundRecord.exception.AlreadyExistsRefundRecordRedis;
import com.nhnacademy.bookstore.purchase.refundRecord.exception.NotExistsRefundRecordRedis;
import com.nhnacademy.bookstore.purchase.refundRecord.repository.RefundRecordRedisRepository;
import com.nhnacademy.bookstore.purchase.refundRecord.repository.RefundRecordRepository;
import com.nhnacademy.bookstore.purchase.refundRecord.service.RefundRecordGuestService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RefundRecordGuestServiceImpl implements RefundRecordGuestService {
	private final RefundRecordRepository refundRecordRepository;
	private final RefundRecordRedisRepository refundRecordRedisRepository;
	private final PurchaseBookRepository purchaseBookRepository;
	private final RefundRepository refundRepository;
	private final PurchaseRepository purchaseRepository;
	private final PurchaseBookCustomRepository purchaseBookCustomRepository;

	@Override
	public Boolean createAllRefundRecordRedis(String orderNumber) {
		if (refundRecordRedisRepository.isHit(orderNumber)) {
			throw new AlreadyExistsRefundRecordRedis();
		}
		List<ReadPurchaseBookResponse> responses = purchaseBookCustomRepository.readGuestBookPurchaseResponses(
			orderNumber);
		for (ReadPurchaseBookResponse response : responses) {
			createRefundRecordRedis(orderNumber, response.id(), response.price(), response.quantity(),
				response.readBookByPurchase());
		}
		return true;
	}

	@Override
	public Long createRefundRecordRedis(String orderNumber, Long purchaseBookId, int price, int quantity,
		ReadBookByPurchase readBookByPurchase) {
		if (refundRecordRedisRepository.isHit(orderNumber)) {
			throw new AlreadyExistsRefundRecordRedis();
		}
		ReadRefundRecordResponse readRefundRecordResponse = ReadRefundRecordResponse.builder()
			.readBookByPurchase(readBookByPurchase)
			.id(purchaseBookId)
			.price(price)
			.quantity(quantity)
			.build();
		return refundRecordRedisRepository.create(orderNumber, purchaseBookId, readRefundRecordResponse);
	}

	@Override
	public Boolean updateRefundRecordAllRedis(String orderNumber) {
		if (!refundRecordRedisRepository.isHit(orderNumber)) {
			return createAllRefundRecordRedis(orderNumber);
		}
		List<ReadPurchaseBookResponse> responses = purchaseBookCustomRepository.readGuestBookPurchaseResponses(
			orderNumber);
		for (ReadPurchaseBookResponse readPurchaseBookResponse : responses) {
			if (!refundRecordRedisRepository.detailIsHit(orderNumber, readPurchaseBookResponse.id())) {
				createRefundRecordRedis(orderNumber, readPurchaseBookResponse.id(), readPurchaseBookResponse.price(),
					readPurchaseBookResponse.quantity(), readPurchaseBookResponse.readBookByPurchase());
			} else {
				refundRecordRedisRepository.update(orderNumber.toString(), readPurchaseBookResponse.id(),
					readPurchaseBookResponse.quantity(),
					readPurchaseBookResponse.price() / readPurchaseBookResponse.quantity());
			}
		}
		return true;

	}

	@Override
	public Boolean updateRefundRecordZeroAllRedis(String orderNumber) {
		if (!refundRecordRedisRepository.isHit(orderNumber)) {
			return createAllRefundRecordRedis(orderNumber);
		}
		List<ReadPurchaseBookResponse> responses = purchaseBookCustomRepository.readGuestBookPurchaseResponses(
			orderNumber);
		for (ReadPurchaseBookResponse readPurchaseBookResponse : responses) {
			if (!refundRecordRedisRepository.detailIsHit(orderNumber, readPurchaseBookResponse.id())) {
				createRefundRecordRedis(orderNumber, readPurchaseBookResponse.id(), 0,
					0, readPurchaseBookResponse.readBookByPurchase());
			} else {
				refundRecordRedisRepository.update(orderNumber.toString(), readPurchaseBookResponse.id(),
					0, 0);
			}
		}
		return true;
	}

	@Override
	public Long updateRefundRecordRedis(String orderNumber, Long purchaseBookId, int quantity) {
		if (!refundRecordRedisRepository.isHit(orderNumber)) {
			throw new NotExistsRefundRecordRedis();
		}
		ReadPurchaseBookResponse purchaseBook = purchaseBookCustomRepository.readPurchaseBookResponse(purchaseBookId);
		return refundRecordRedisRepository.update(orderNumber, purchaseBookId, quantity,
			purchaseBook.price() / purchaseBook.quantity());
	}

	@Override
	public Long deleteRefundRecordRedis(String orderNumber, Long purchaseBookId) {
		if (!refundRecordRedisRepository.isHit(orderNumber)) {
			throw new NotExistsRefundRecordRedis();
		}
		return refundRecordRedisRepository.delete(orderNumber, purchaseBookId);
	}

	@Override
	public List<ReadRefundRecordResponse> readRefundRecordRedis(String orderNumber) {
		if (!refundRecordRedisRepository.isHit(orderNumber)) {
			throw new NotExistsRefundRecordRedis();
		}

		return refundRecordRedisRepository.readAll(orderNumber);
	}

	@Override
	public Boolean createRefundRecord(String orderNumber, Long refundId) {
		if (!refundRecordRedisRepository.isHit(orderNumber)) {
			return false;
		}
		Refund memberRefund = refundRepository.findById(refundId).orElseThrow(NotExistsRefund::new);

		List<ReadRefundRecordResponse> readRefundRecordResponseList = readRefundRecordRedis(orderNumber);

		for (ReadRefundRecordResponse readRefundRecordResponse : readRefundRecordResponseList) {
			RefundRecord refundRecord = new RefundRecord();
			refundRecord.setRefund(memberRefund);
			refundRecord.setPurchaseBook(purchaseBookRepository.findById(readRefundRecordResponse.id()).orElseThrow(
				NotExistsPurchaseBook::new));
			refundRecord.setPrice(readRefundRecordResponse.price());
			refundRecord.setQuantity(readRefundRecordResponse.quantity());
			refundRecordRepository.save(refundRecord);
		}
		refundRecordRedisRepository.deleteAll(orderNumber);

		return true;

	}

}
