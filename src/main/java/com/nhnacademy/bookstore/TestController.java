package com.nhnacademy.bookstore;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstore.global.keyManager.manager.KeyManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {
	private final KeyManager keyManager;

	@GetMapping("/test")
	public String test() {
		// Logger logger = LoggerFactory.getLogger("errorLogger");
		// logger.error("3Runner 화이팅!");
		// KeyManagerResponse key = keyManager.getKey("b6b9d1cb45ab4fb1b46637352b6a053c");

		// return key.getBody().getSecret();
		return "test";
	}

	@GetMapping("/exception")
	public String exception() {
		throw new RuntimeException("Test exception");
	}

}
