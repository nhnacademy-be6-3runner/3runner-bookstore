package com.nhnacademy.bookstore.book.book.controller.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "ApiBookImageClient")
public interface ApiBookImageClient {
}
