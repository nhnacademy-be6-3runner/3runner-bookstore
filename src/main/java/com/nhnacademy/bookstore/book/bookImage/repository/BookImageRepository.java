package com.nhnacademy.bookstore.book.bookImage.repository;

import com.nhnacademy.bookstore.entity.bookImage.BookImage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookImageRepository extends JpaRepository<BookImage, Long> {
}
