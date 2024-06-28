package com.nhnacademy.bookstore.purchase.purchase.dto.request;

import com.nhnacademy.bookstore.entity.purchase.enums.PurchaseStatus;
import lombok.Builder;


@Builder
public record UpdatePurchaseMemberRequest(PurchaseStatus purchaseStatus) {
}
