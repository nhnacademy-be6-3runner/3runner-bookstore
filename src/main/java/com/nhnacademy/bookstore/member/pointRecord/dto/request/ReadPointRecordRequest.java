package com.nhnacademy.bookstore.member.pointRecord.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.ZonedDateTime;

/**
 * 포인트 요청 Dto.
 *
 * @author 김병우
 * @param page 페이지
 * @param size 사이즈
 */
@Builder
public record ReadPointRecordRequest(
        int page,
        int size,
        String sort) {
    }
