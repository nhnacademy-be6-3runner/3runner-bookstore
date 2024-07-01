package com.nhnacademy.bookstore;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class TestController {
	@GetMapping("/bookstore/test")
	public String test(@RequestHeader(name = "Member-Id") String memberId) {
		log.warn("Member-Id Header: {}", memberId);
		return "test!";
	}
}
