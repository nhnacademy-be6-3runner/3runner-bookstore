package com.nhnacademy.bookstore.book.bookCartegory.service.impl;

import com.nhnacademy.bookstore.book.book.dto.response.BookListResponse;
import com.nhnacademy.bookstore.book.book.exception.BookDoesNotExistException;
import com.nhnacademy.bookstore.book.book.repository.BookRepository;
import com.nhnacademy.bookstore.book.bookCartegory.dto.request.CreateBookCategoryRequest;
import com.nhnacademy.bookstore.book.bookCartegory.dto.request.UpdateBookCategoryRequest;
import com.nhnacademy.bookstore.book.bookCartegory.dto.response.BookCategoriesResponse;
import com.nhnacademy.bookstore.book.bookCartegory.exception.BookCategoryAlreadyExistsException;
import com.nhnacademy.bookstore.book.bookCartegory.exception.BookCategoryNotFoundException;
import com.nhnacademy.bookstore.book.bookCartegory.repository.BookCategoryRepository;
import com.nhnacademy.bookstore.book.bookCartegory.service.BookCategoryService;
import com.nhnacademy.bookstore.book.category.exception.CategoryNotFoundException;
import com.nhnacademy.bookstore.book.category.repository.CategoryRepository;
import com.nhnacademy.bookstore.entity.book.Book;
import com.nhnacademy.bookstore.entity.bookCategory.BookCategory;
import com.nhnacademy.bookstore.entity.category.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 도서-카테고리 service
 * @author 김은비
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BookCategoryServiceImpl implements BookCategoryService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookCategoryRepository bookCategoryRepository;

    /**
     * 도서-카테고리 생성 메서드
     * @param dto 생성 내용
     */
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void createBookCategory(CreateBookCategoryRequest dto) {
        Book book = getBookById(dto.bookId());
        List<Category> categories = getCategoriesByIds(dto.categoryIds());

        for (Category category : categories) {
            if (bookCategoryRepository.existsByBookAndCategory(book, category)) {
                throw new BookCategoryAlreadyExistsException("이미 등록된 카테고리입니다.");
            }
        }

        for (Category category : categories) {
            BookCategory bookCategory = BookCategory.create(book, category);
            book.addBookCategory(bookCategory);
        }

        bookRepository.save(book);
    }

    /**
     * 도서-카테고리 수정 메서드
     * @param dto 수정 내용
     */
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void updateBookCategory(long bookId, UpdateBookCategoryRequest dto) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new BookDoesNotExistException("존재하지 않는 도서입니다."));
        List<Category> categories = categoryRepository.findAllById(dto.categoryIds());
        if (categories.size() != dto.categoryIds().size()) {
            throw new CategoryNotFoundException("존재하지 않는 카테고리입니다.");
        }

        // 기존의 모든 카테고리 삭제
        book.getBookCategoryList().clear();

        for (Category category : categories) {
            BookCategory bookCategory = BookCategory.create(book, category);
            book.addBookCategory(bookCategory);
        }

        // 엔티티의 상태 변경이 반영되도록 book을 save
        bookRepository.save(book);
    }


    @Override
    public void deletedBookCategory(Long id) {
        BookCategory bookCategory = bookCategoryRepository.findById(id)
            .orElseThrow(() -> new BookCategoryNotFoundException("도서에 등록되지 않은 카테고리입니다."));

        Book book = bookCategory.getBook();
        book.removeBookCategory(bookCategory);

        bookCategoryRepository.deleteById(id);
    }


    /**
     * 도서에 해당하는 카테고리 목록 불러오는 메서드
     * @param bookId 책 아이디
     * @return 책에 해당하는 카테고리 list
     */
    @Override
    public List<BookCategoriesResponse> readBookWithCategoryList(Long bookId) {
        return bookCategoryRepository.bookWithCategoryList(bookId);
    }

    /**
     * 카테고리에 해당하는 도서 목록 불러오는 메서드
     * @param categoryIds 카테고리 아이디 목록
     * @param pageable 페이지
     * @return 카테고리에 해당하는 도서 list
     */
    @Override
    public Page<BookListResponse> readCategoriesWithBookList(List<Long> categoryIds, Pageable pageable) {
        List<Category> categories = categoryRepository.findAllById(categoryIds);
        if (categories.size() != categoryIds.size()) {
            throw new CategoryNotFoundException("존재하지 않는 카테고리가 있습니다.");
        }
        return bookCategoryRepository.categoriesWithBookList(categoryIds, pageable);
    }

    private Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
            .orElseThrow(() -> new BookDoesNotExistException("존재하지 않는 책입니다."));
    }

    private List<Category> getCategoriesByIds(List<Long> categoryIds) {
        List<Category> categories = categoryRepository.findAllById(categoryIds);
        if (categories.size() != categoryIds.size()) {
            throw new CategoryNotFoundException("존재하지 않는 카테고리입니다.");
        }
        return categories;
    }
}
