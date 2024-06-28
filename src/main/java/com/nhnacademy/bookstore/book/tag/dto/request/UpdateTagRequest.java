package com.nhnacademy.bookstore.book.tag.dto.request;

import lombok.Builder;

/**
 * 태그수정 dto
 * @author 정주혁
 */
@Builder
public record UpdateTagRequest(Long tagId, String tagName) {
}
