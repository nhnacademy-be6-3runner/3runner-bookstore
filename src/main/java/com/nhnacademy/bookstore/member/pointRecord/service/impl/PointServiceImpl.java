package com.nhnacademy.bookstore.member.pointRecord.service.impl;

import com.nhnacademy.bookstore.entity.pointRecord.PointRecord;
import com.nhnacademy.bookstore.member.pointRecord.repository.PointRecordRepository;
import com.nhnacademy.bookstore.member.pointRecord.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class PointServiceImpl implements PointService {
    private final PointRecordRepository pointRecordRepository;

    /**
     * @Author -유지아
     * Save point record. -point가 추가,사용 되었을때 레코드를 저장한다.
     *
     * @param pointRecord the point record -pointrecord를 받아온다.
     * @return the point record -저장한 포인트값반환 - 저장한 후 그대로 반환한다.
     */
    public PointRecord save(PointRecord pointRecord) {
        return pointRecordRepository.save(pointRecord);
    }
}
