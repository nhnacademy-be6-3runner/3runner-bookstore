package com.nhnacademy.bookstore.book.tag.service;

import com.nhnacademy.bookstore.book.tag.dto.request.CreateTagRequest;
import com.nhnacademy.bookstore.book.tag.dto.request.DeleteTagRequest;
import com.nhnacademy.bookstore.book.tag.dto.request.UpdateTagRequest;
import com.nhnacademy.bookstore.book.tag.dto.response.TagResponse;
import com.nhnacademy.bookstore.entity.tag.Tag;
import java.util.List;

/**
 * Tag CRUD 서비스
 * @author 정주혁
 */
public interface TagService {

    List<TagResponse> getAllTags();
    Long createTag(CreateTagRequest tag);
    void deleteTag(DeleteTagRequest tag);
    Long updateTag(UpdateTagRequest tag);
}
