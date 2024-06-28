package com.nhnacademy.bookstore.book.tag.repository;

import com.nhnacademy.bookstore.entity.tag.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    private Tag tag;

    @BeforeEach
    public void setUp() {
        tag = new Tag();
        tag.setName("Sample Tag");
        tagRepository.save(tag);
    }

    @Test
    public void testFindByName() {
        Optional<Tag> foundTag = tagRepository.findByName("Sample Tag");

        assertThat(foundTag).isPresent();
        assertThat(foundTag.get().getName()).isEqualTo(tag.getName());
    }


}
