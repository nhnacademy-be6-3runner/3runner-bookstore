package com.nhnacademy.bookstore.member.pointRecord.controller;

import com.nhnacademy.bookstore.member.pointRecord.dto.request.ReadPointRecordRequest;
import com.nhnacademy.bookstore.member.pointRecord.dto.response.ReadPointRecordResponse;
import com.nhnacademy.bookstore.member.pointRecord.service.PointRecordService;
import com.nhnacademy.bookstore.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * 포인트레코드 컨트롤러.
 *
 * @author 김병우
 */
@RestController
@RequiredArgsConstructor
public class PointRecordController {
    private final PointRecordService pointRecordService;

    /**
     * 맴버의 포인트 내역 출력
     *
     * @param memberId 맴버아이디
     * @return 포인트내역Dto리스트
     */
    @GetMapping("/bookstore/members/points")
    public ApiResponse<Page<ReadPointRecordResponse>> readPointRecord(
            @RequestHeader Long memberId,
            @RequestBody ReadPointRecordRequest readPointRecordRequest
            ) {

        Pageable pageable;
        if (!Objects.isNull(readPointRecordRequest.sort())) {
            pageable = PageRequest.of(
                    readPointRecordRequest.page(),
                    readPointRecordRequest.page(),
                    Sort.by(readPointRecordRequest.sort())
            );
        } else {
            pageable = PageRequest.of(readPointRecordRequest.page(), readPointRecordRequest.size());
        }

        return ApiResponse.success(pointRecordService.readByMemberId(memberId, pageable));
    }
}
