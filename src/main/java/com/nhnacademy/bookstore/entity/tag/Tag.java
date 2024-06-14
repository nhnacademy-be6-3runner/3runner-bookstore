package com.nhnacademy.bookstore.entity.tag;

import com.nhnacademy.bookstore.entity.bookTag.BookTag;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Size(min = 1, max = 100)
    private String name;

    //연결

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookTag> bookTagSet = new HashSet<>();
}
