package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.skypro.homework.controller.CommentController;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.service.CommentService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private CommentMapper commentMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CommentController(commentService, commentMapper))
                .build();
    }

    @Test
    public void testGetCommentsByAdsId() throws Exception {
        // Mock data
        List<CommentDTO> comments = Arrays.asList(new CommentDTO(), new CommentDTO());
        CommentsDTO commentsDTO = new CommentsDTO();
        commentsDTO.setCount(comments.size()+1);
        commentsDTO.setResults(comments);

        when(commentService.getAllComment(ArgumentMatchers.anyInt())).thenReturn(comments);

        // Perform GET request and verify the response
        mockMvc.perform(get("/ads/{id}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(comments.size()-1))
                .andExpect(jsonPath("$.results.length()").value(comments.size()));
    }

    @Test
    public void testAddComment() throws Exception {
        // Mock data
        CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment();
        createOrUpdateComment.setText("Test comment");

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setText("Test comment");

        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setText("Test comment");

        when(commentMapper.toComment(createOrUpdateComment)).thenReturn(commentEntity);
        when(commentService.createComment(ArgumentMatchers.anyInt(), ArgumentMatchers.any(CommentEntity.class))).thenReturn(commentDTO);

        // Perform POST request and verify the response
        mockMvc.perform(post("/ads/{id}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createOrUpdateComment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.text").value("Test comment"));
    }

    @Test
    public void testDeleteCommentsByAdsId() throws Exception {
        // Perform DELETE request and verify the response
        mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(commentService, times(1)).deleteComment(ArgumentMatchers.eq(1), ArgumentMatchers.eq(1));
    }

    @Test
    public void testUpdateComment() throws Exception {
        // Mock data
        CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment();
        createOrUpdateComment.setText("Updated comment");

        CommentDTO updatedCommentDTO = new CommentDTO();
        updatedCommentDTO.setText("Updated comment");

        when(commentService.patchCommentId(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), ArgumentMatchers.any(CreateOrUpdateComment.class)))
                .thenReturn(updatedCommentDTO);

        // Perform PATCH request and verify the response
        mockMvc.perform(patch("/ads/{adId}/comments/{commentId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createOrUpdateComment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Updated comment"));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
