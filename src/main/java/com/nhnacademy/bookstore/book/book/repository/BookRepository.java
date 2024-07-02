package com.nhnacademy.bookstore.book.book.repository;

import com.nhnacademy.bookstore.entity.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 책 Repository.
 *
 * @author 김병우
 */
public interface BookRepository extends JpaRepository<Book, Long>, BookCustomRepository {
}
