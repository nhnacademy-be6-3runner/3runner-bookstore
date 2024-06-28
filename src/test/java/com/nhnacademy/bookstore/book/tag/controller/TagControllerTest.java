package com.nhnacademy.bookstore.book.tag.controller;

import com.nhnacademy.bookstore.book.tag.dto.request.CreateTagRequest;
import com.nhnacademy.bookstore.book.tag.dto.request.DeleteTagRequest;
import com.nhnacademy.bookstore.book.tag.dto.request.UpdateTagRequest;
import com.nhnacademy.bookstore.book.tag.dto.response.TagResponse;
import com.nhnacademy.bookstore.book.tag.service.TagService;
import com.nhnacademy.bookstore.util.ApiResponse;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TagControllerTest {

    @Mock
    private TagService tagService;

    @InjectMocks
    private TagController tagController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReadAll(){
        List<TagResponse> tagResponseList = new ArrayList<>();
        tagResponseList.add(TagResponse.builder().id(1L).name("Tag1").build());
        tagResponseList.add(TagResponse.builder().id(2L).name("Tag2").build());
        tagResponseList.add(new TagResponse(3L, "Tag3"));

        when(tagService.getAllTags()).thenReturn(tagResponseList);

        ApiResponse<List<TagResponse>> getTagResponseApiResponse = tagController.getAllTags();

        assertEquals(200, getTagResponseApiResponse.getHeader().getResultCode());
        assertEquals(3, getTagResponseApiResponse.getBody().getData().size());
    }

    @Test
    void testCreateTag() {
        // Mock data
        CreateTagRequest createTagRequest = CreateTagRequest.builder().name("Test Tag").build();

        // Mock service method
        Long expectedId = 1L;
        when(tagService.createTag(any(CreateTagRequest.class))).thenReturn(expectedId);

        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "createTagRequest");

        // Call controller method
        ApiResponse<Long> responseEntity = tagController.createTag(createTagRequest, bindingResult);

        // Verify
        assertEquals(201, responseEntity.getHeader().getResultCode());
        assertTrue(responseEntity.getHeader().isSuccessful());
        assertEquals(expectedId, responseEntity.getBody().getData());

        verify(tagService).createTag(createTagRequest);
    }

    @Test
    void testDeleteTag() {
        // Mock data
        DeleteTagRequest deleteTagRequest = DeleteTagRequest.builder().tagId(1L).build();

        // Mock service method
        doNothing().when(tagService).deleteTag(any(DeleteTagRequest.class));
        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "deleteTagRequest");

        // Call controller method
        ApiResponse<Void> responseEntity = tagController.deleteTag(deleteTagRequest,bindingResult);

        // Verify
        assertEquals(204, responseEntity.getHeader().getResultCode());
        assertTrue(responseEntity.getHeader().isSuccessful());
        verify(tagService).deleteTag(deleteTagRequest);
    }

    @Test
    void testUpdateTag() {
        // Mock data
        UpdateTagRequest updateTagRequest = UpdateTagRequest.builder().tagId(1L).tagName("Updated Tag").build();

        // Mock service method
        Long expectedId = 1L;
        when(tagService.updateTag(any(UpdateTagRequest.class))).thenReturn(expectedId);

        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "updateTagRequest");

        // Call controller method
        ApiResponse<Long> responseEntity = tagController.updateTag(updateTagRequest, bindingResult);

        // Verify
        assertEquals(200, responseEntity.getHeader().getResultCode());
        assertTrue(responseEntity.getHeader().isSuccessful());
        assertEquals(expectedId, responseEntity.getBody().getData());

        verify(tagService).updateTag(updateTagRequest);
    }
}
