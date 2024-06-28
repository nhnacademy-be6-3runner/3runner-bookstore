package com.nhnacademy.bookstore.book.bookCartegory.repository;

import com.nhnacademy.bookstore.entity.book.Book;
import com.nhnacademy.bookstore.entity.bookCategory.BookCategory;
import com.nhnacademy.bookstore.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookCategoryRepository extends JpaRepository<BookCategory, Long>, BookCategoryCustomRepository {
    void deleteByBook(Book book);
    boolean existsByBookAndCategory(Book book, Category category);
}
