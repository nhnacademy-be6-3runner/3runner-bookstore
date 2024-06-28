package com.nhnacademy.bookstore.member.address.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * @Author -유지아
 * The type Create address request. -주소 추가에 대한 요청 record이다.
 */
@Builder
public record CreateAddressRequest(
        @NotNull @Size(min = 1, max = 20) String name,
        @NotNull @Size(min = 1, max = 100) String country,
        @NotNull @Size(min = 1, max = 100) String city,
        @NotNull @Size(min = 1, max = 100) String state,
        @NotNull @Size(min = 1, max = 100) String road,
        @NotNull @Size(min = 1, max = 20) String postalCode
) {
}
