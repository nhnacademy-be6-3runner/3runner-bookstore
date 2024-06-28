package com.nhnacademy.bookstore.member.address.controller;

import com.nhnacademy.bookstore.entity.address.Address;
import com.nhnacademy.bookstore.entity.member.Member;
import com.nhnacademy.bookstore.member.address.dto.request.CreateAddressRequest;
import com.nhnacademy.bookstore.member.address.dto.request.UpdateAddressRequest;
import com.nhnacademy.bookstore.member.address.dto.response.UpdateAddressResponse;
import com.nhnacademy.bookstore.member.address.service.impl.AddressServiceImpl;
import com.nhnacademy.bookstore.member.member.service.MemberService;
import com.nhnacademy.bookstore.member.member.service.impl.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AddressControllerTest {

    MockMvc mockMvc;

    @MockBean
    private AddressServiceImpl addressServiceImpl;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private MemberServiceImpl memberServiceImpl;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

    }

    @Test
    @DisplayName("주소 생성 테스트")
    void createAddress() throws Exception {
        Long memberId = 1L;
        CreateAddressRequest createAddressRequest = new CreateAddressRequest("집","Country", "City", "State", "Road", "12345");
        Member member = new Member();
        member.setId(memberId);

        Address address = new Address(createAddressRequest, member);

        when(memberServiceImpl.readById(anyLong())).thenReturn(member);
        doNothing().when(addressServiceImpl).save(Mockito.<Address>any(), Mockito.<Member>any());
        when(addressServiceImpl.readAll(any(Member.class))).thenReturn(Arrays.asList(address));

        String requestJson = "{ \"name\": \"Name\", \"country\": \"Country\", \"city\": \"City\", \"state\": \"State\", \"road\": \"Road\",\"postalCode\": \"12345\" }";

        mockMvc.perform(post("/bookstore/members/addresses")
                        .header("member-id", memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print());
                //.andExpect(status().isCreated());
    }
    @Test
    @DisplayName("회원 주소 조회")
    void readAddress() throws Exception {
        Long memberId = 1L;
        CreateAddressRequest createAddressRequest = new CreateAddressRequest("집","Country", "City", "State", "Road", "12345");
        Member member = new Member();
        member.setId(memberId);
        Address address = new Address(createAddressRequest, member);
        when(memberServiceImpl.readById(anyLong())).thenReturn(member);
        when(addressServiceImpl.readAll(any(Member.class))).thenReturn(Arrays.asList(address));
        mockMvc.perform(get("/bookstore/members/addresses")
                        .header("member-id", memberId)
                        )
                .andDo(print());

    }
    @Test
    @DisplayName("주소 업데이트 테스트")
    void updateAddress() throws Exception {
        Address address = Address.builder()
                .name("New Address Name")
                .build();

        UpdateAddressResponse updateAddressResponse = UpdateAddressResponse.builder()
                .id(1L)
                .name("New Address Name")
                .build();

        when(addressServiceImpl.updateAddress(anyLong(), any(UpdateAddressRequest.class)))
                .thenReturn(address);

        String requestJson = "{ \"name\": \"New Address Name\", \"country\": \"Country\", \"city\": \"City\", \"state\": \"State\", \"road\": \"Road\", \"postalCode\": \"12345\" }";

        mockMvc.perform(MockMvcRequestBuilders.put("/bookstore/members/addresses")
                        .header("Address-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("주소 삭제 테스트")
    void deleteAddress() throws Exception {
        Mockito.doNothing().when(addressServiceImpl).deleteAddress(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/bookstore/members/addresses")
                        .header("Address-Id", 1L))
                .andDo(print())
                .andExpect(content().json("{\"header\":{\"resultCode\":204,\"successful\":true}}"));
    }
}
