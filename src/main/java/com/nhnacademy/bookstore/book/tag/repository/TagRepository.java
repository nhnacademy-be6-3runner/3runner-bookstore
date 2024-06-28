package com.nhnacademy.bookstore.book.tag.repository;

import com.nhnacademy.bookstore.entity.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 태그 CRUD jpa
 * @author 정주혁
 */
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
}
