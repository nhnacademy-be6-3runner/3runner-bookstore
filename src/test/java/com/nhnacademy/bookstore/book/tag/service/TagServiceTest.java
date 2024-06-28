package com.nhnacademy.bookstore.book.tag.service;

import com.nhnacademy.bookstore.book.tag.dto.request.CreateTagRequest;
import com.nhnacademy.bookstore.book.tag.dto.request.DeleteTagRequest;
import com.nhnacademy.bookstore.book.tag.dto.request.UpdateTagRequest;
import com.nhnacademy.bookstore.book.tag.dto.response.TagResponse;
import com.nhnacademy.bookstore.book.tag.exception.AlreadyHaveTagException;
import com.nhnacademy.bookstore.book.tag.exception.NotExistsTagException;
import com.nhnacademy.bookstore.book.tag.repository.TagRepository;
import com.nhnacademy.bookstore.book.tag.service.Impl.TagServiceImpl;
import com.nhnacademy.bookstore.entity.tag.Tag;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllTagsTest(){
        List<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag(1, "Tag1",null));
        tagList.add(new Tag(2, "Tag2",null));
        tagList.add(new Tag(3, "Tag3",null));
        when(tagRepository.findAll()).thenReturn(tagList);

        List<TagResponse> allTags = tagService.getAllTags();

        assertThat(allTags.size()).isEqualTo(tagList.size());
        assertThat(allTags.getFirst().id()).isEqualTo(tagList.getFirst().getId());
        assertThat(allTags.getFirst().name()).isEqualTo(tagList.getFirst().getName());

    }

    @Test
    public void testCreateTag_Success() {
        CreateTagRequest createTagRequest = CreateTagRequest.builder()
                .name("New Tag")
                .build();

        when(tagRepository.findByName(createTagRequest.name())).thenReturn(Optional.empty());
        when(tagRepository.save(any(Tag.class))).thenReturn(new Tag());

        tagService.createTag(createTagRequest);

        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    @Test
    public void testDeleteTag_Success() {
        DeleteTagRequest deleteTagRequest = DeleteTagRequest.builder()
                .tagId(1L)
                .build();

        when(tagRepository.existsById(deleteTagRequest.tagId())).thenReturn(true);
        doNothing().when(tagRepository).deleteById(deleteTagRequest.tagId());

        tagService.deleteTag(deleteTagRequest);

        verify(tagRepository, times(1)).deleteById(deleteTagRequest.tagId());
    }

    @Test
    public void testUpdateTag_Success() {
        UpdateTagRequest updateTagRequest = UpdateTagRequest.builder()
                .tagId(1L)
                .tagName("Updated Tag Name")
                .build();

        Tag existingTag = new Tag();
        existingTag.setName("Old Tag Name");

        when(tagRepository.findById(updateTagRequest.tagId())).thenReturn(Optional.of(existingTag));
        when(tagRepository.save(any(Tag.class))).thenReturn(existingTag);

        tagService.updateTag(updateTagRequest);

        verify(tagRepository, times(1)).save(existingTag);
    }


    @Test
    void createTag_alreadyHaveTagExceptionThrown() {
        // Given
        CreateTagRequest request = new CreateTagRequest("existingTag");
        Tag existingTag = new Tag();
        existingTag.setName("existingTag");

        // When
        when(tagRepository.findByName("existingTag")).thenReturn(Optional.of(existingTag));

        // Then
        AlreadyHaveTagException exception = assertThrows(AlreadyHaveTagException.class, () -> {
            tagService.createTag(request);
        });

        assertEquals("태그가 이미 있습니다.", exception.getMessage());
        verify(tagRepository, never()).save(any(Tag.class));
    }

    @Test
    void deleteTagExceptionThrown() {
        when(tagRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(NotExistsTagException.class, () -> tagService.deleteTag(new DeleteTagRequest(1L)));
    }

    @Test
    void updateTagExceptionThrown() {
        when(tagRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(NotExistsTagException.class, () -> tagService.updateTag(new UpdateTagRequest(1L, "update")));
    }

}
