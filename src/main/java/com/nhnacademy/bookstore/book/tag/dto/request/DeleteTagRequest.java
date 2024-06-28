package com.nhnacademy.bookstore.book.tag.dto.request;

import lombok.Builder;

/**
 * 태그제거 dto
 * @author 정주혁
 */
@Builder
public record DeleteTagRequest(Long tagId) {

}
