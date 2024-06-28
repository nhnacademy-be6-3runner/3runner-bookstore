package com.nhnacademy.bookstore.purchase.purchaseBook.dto.response;

import com.nhnacademy.bookstore.entity.bookCategory.BookCategory;
import com.nhnacademy.bookstore.entity.bookImage.BookImage;
import com.nhnacademy.bookstore.entity.bookTag.BookTag;
import lombok.Builder;

import java.util.List;

/**
 * 주문-책 조회시 필요한 책 정보들
 *
 * @author 정주혁
 *
 * @param title
 * @param description
 * @param price
 * @param sellingPrice
 * @param packing
 * @param publisher
 * @param bookCategories
 * @param bookTags
 * @param bookImages
 */
@Builder
public record ReadBookByPurchase(String title, String description, int price,
                                         int sellingPrice, boolean packing, String publisher,
                                         List<BookCategory> bookCategories, List<BookTag> bookTags,
                                         List<BookImage> bookImages) {
}
