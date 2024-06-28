package com.nhnacademy.bookstore.book.tag.dto.request;

import lombok.Builder;

/**
 * 태그생성 dto
 * @author 정주혁
 */
@Builder
public record CreateTagRequest(String name) {
}
