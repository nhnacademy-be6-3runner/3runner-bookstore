package com.nhnacademy.bookstore.book.bookImage.dto.request;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.nhnacademy.bookstore.entity.bookImage.enums.BookImageType;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class CreateBookImageRequestTest {

	private static Validator validator;

	@BeforeAll
	static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	void validCreateBookImageRequest() {
		CreateBookImageRequest request = CreateBookImageRequest.builder()
			.url("http://example.com/image.jpg")
			.type(BookImageType.MAIN)
			.bookId(1L)
			.build();

		Set<ConstraintViolation<CreateBookImageRequest>> violations = validator.validate(request);
		assertThat(violations).isEmpty();
	}

	@Test
	void invalidCreateBookImageRequest_NullUrl() {
		CreateBookImageRequest request = CreateBookImageRequest.builder()
			.url(null)
			.type(BookImageType.MAIN)
			.bookId(1L)
			.build();

		Set<ConstraintViolation<CreateBookImageRequest>> violations = validator.validate(request);
		assertThat(violations).isNotEmpty();
		assertThat(violations).anyMatch(
			v -> v.getPropertyPath().toString().equals("url") && v.getMessage().contains("공백일 수 없습니다"));
	}

	@Test
	void invalidCreateBookImageRequest_BlankUrl() {
		CreateBookImageRequest request = CreateBookImageRequest.builder()
			.url("")
			.type(BookImageType.DESCRIPTION)
			.bookId(1L)
			.build();

		Set<ConstraintViolation<CreateBookImageRequest>> violations = validator.validate(request);
		assertThat(violations).isNotEmpty();
		assertThat(violations).anyMatch(
			v -> v.getPropertyPath().toString().equals("url") && v.getMessage().contains("공백일 수 없습니다"));
	}

	@Test
	void invalidCreateBookImageRequest_TooLongUrl() {
		CreateBookImageRequest request = CreateBookImageRequest.builder()
			.url("http://example.com/" + "a".repeat(51))
			.type(BookImageType.DESCRIPTION)
			.bookId(1L)
			.build();

		Set<ConstraintViolation<CreateBookImageRequest>> violations = validator.validate(request);
		assertThat(violations).isNotEmpty();
		assertThat(violations).anyMatch(
			v -> v.getPropertyPath().toString().equals("url") && v.getMessage().contains("크기가 0에서 50 사이여야 합니다"));
	}

	@Test
	void invalidCreateBookImageRequest_NullType() {
		CreateBookImageRequest request = CreateBookImageRequest.builder()
			.url("http://example.com/image.jpg")
			.type(null)
			.bookId(1L)
			.build();

		Set<ConstraintViolation<CreateBookImageRequest>> violations = validator.validate(request);
		assertThat(violations).isNotEmpty();
		assertThat(violations).anyMatch(
			v -> v.getPropertyPath().toString().equals("type") && v.getMessage().contains("널이어서는 안됩니다"));
	}

	@Test
	void invalidCreateBookImageRequest_NullBookId() {
		CreateBookImageRequest request = CreateBookImageRequest.builder()
			.url("http://example.com/image.jpg")
			.type(BookImageType.DESCRIPTION)
			.bookId(0L)
			.build();

		Set<ConstraintViolation<CreateBookImageRequest>> violations = validator.validate(request);
		assertThat(violations).isEmpty();  // Assuming that bookId=0 is valid, modify if your validation logic changes
	}
}
