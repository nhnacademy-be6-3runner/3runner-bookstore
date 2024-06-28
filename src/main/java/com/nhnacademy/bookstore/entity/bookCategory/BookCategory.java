package com.nhnacademy.bookstore.entity.bookCategory;

import com.nhnacademy.bookstore.entity.book.Book;
import com.nhnacademy.bookstore.entity.category.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Table(name = "book_category", indexes = {
    @Index(name = "idx_book_id", columnList = "book_id"),
    @Index(name = "idx_category_id", columnList = "category_id")
})
public class BookCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @Setter
    private Book book;

    @ManyToOne
    @Setter
    private Category category;

    public static BookCategory create(Book book, Category category) {
        BookCategory bookCategory = BookCategory.builder()
                .book(book)
                .category(category)
                .build();
        book.addBookCategory(bookCategory);  // 양방향 설정
        return bookCategory;
    }
}
