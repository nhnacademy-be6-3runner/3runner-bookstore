package com.nhnacademy.bookstore.member.pointRecord.controller;

import com.nhnacademy.bookstore.member.pointRecord.dto.response.ReadPointRecordResponse;
import com.nhnacademy.bookstore.member.pointRecord.service.PointRecordService;
import com.nhnacademy.bookstore.member.pointRecord.service.impl.PointRecordServiceImpl;
import com.nhnacademy.bookstore.util.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PointRecordController.class)
class PointRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointRecordService pointRecordService;


    @Test
    void testReadPointRecord() throws Exception {
//        ReadPointRecordResponse response1 = ReadPointRecordResponse.builder()
//                .recordId(1L)
//                .usePoint(10L)
//                .createdAt(ZonedDateTime.now())
//                .content("Test1")
//                .build();
//
//        ReadPointRecordResponse response2 = ReadPointRecordResponse.builder()
//                .recordId(2L)
//                .usePoint(20L)
//                .createdAt(ZonedDateTime.now())
//                .content("Test2")
//                .build();
//
//        Page<ReadPointRecordResponse> responses = Pageable;
//
//        when(pointRecordService.readByMemberId(anyLong(),any())).thenReturn(responses);
//
//        mockMvc.perform(get("/bookstore/members/points")
//                        .header("memberId", 1L)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.body.data[0].recordId").value(1L))
//                .andExpect(jsonPath("$.body.data[0].usePoint").value(10L))
//                .andExpect(jsonPath("$.body.data[0].content").value("Test1"))
//                .andExpect(jsonPath("$.body.data[1].recordId").value(2L))
//                .andExpect(jsonPath("$.body.data[1].usePoint").value(20L))
//                .andExpect(jsonPath("$.body.data[1].content").value("Test2"));
    }
}