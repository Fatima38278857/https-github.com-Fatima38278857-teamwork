package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.NotFoundCommentException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.impl.CommentServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTests {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private AdRepository adRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    public void testGetAllComment() {
        // Mock data
        List<CommentEntity> mockComments = new ArrayList<>();
        mockComments.add(new CommentEntity());
        mockComments.add(new CommentEntity());

        when(commentRepository.findAll()).thenReturn(mockComments);
        when(commentMapper.toDto(any(CommentEntity.class))).thenReturn(new CommentDTO());

        // Call service method
        List<CommentDTO> result = commentService.getAllComment(1);

        // Assertions
        assertEquals(mockComments.size(), result.size());
        verify(commentRepository, times(1)).findAll();
        verify(commentMapper, times(mockComments.size())).toDto(any(CommentEntity.class));
    }

    @Test
    public void testCreateComment() {
        // Mock data
        UserDto mockUser = new UserDto(1, "John", "Doe", "john.doe@example.com", "image.jpg");
        Optional<Ad> mockAd = Optional.of(new Ad());
        CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment();
        createOrUpdateComment.setText("Test comment");

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setText("Test comment");

        when(userService.getUser()).thenReturn(mockUser);
        when(adRepository.findById(anyInt())).thenReturn(mockAd);
        when(commentRepository.save(any(CommentEntity.class))).thenReturn(commentEntity);
        when(commentMapper.toDto(any(CommentEntity.class))).thenReturn(new CommentDTO());

        // Call service method
        CommentDTO result = commentService.createComment(1, commentEntity);

        // Assertions
        assertNotNull(result);
        assertEquals("Test comment", result.getText());
        verify(userService, times(1)).getUser();
        verify(adRepository, times(1)).findById(anyInt());
        verify(commentRepository, times(1)).save(any(CommentEntity.class));
        verify(commentMapper, times(1)).toDto(any(CommentEntity.class));
    }

    @Test
    public void testCreateComment_AdNotFoundException() {
        // Mock data
        when(adRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Call service method and assert exception
        assertThrows(AdNotFoundException.class, () -> commentService.createComment(1, new CommentEntity()));
    }

    @Test
    public void testDeleteComment() {
        // Call service method
        commentService.deleteComment(1, 1);

        // Verify repository method was called
        verify(commentRepository, times(1)).deleteCommentByAdIdAndPk(anyInt(), anyInt());
    }

    @Test
    public void testPatchCommentId() {
        // Mock data
        UserDto mockUser = new UserDto(1, "John", "Doe", "john.doe@example.com", "image.jpg");
        CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment();
        createOrUpdateComment.setText("Updated comment");

        CommentEntity mockCommentEntity = new CommentEntity();
        mockCommentEntity.setText("Updated comment");

        when(userService.getUser()).thenReturn(mockUser);
        when(commentRepository.findAllCommentByAdIdAndAuthorIdAndIdComment(anyInt(), anyInt(), anyInt())).thenReturn(mockCommentEntity);
        when(commentRepository.save(any(CommentEntity.class))).thenReturn(mockCommentEntity);
        when(commentMapper.toDto(any(CommentEntity.class))).thenReturn(new CommentDTO());

        // Call service method
        CommentDTO result = commentService.patchCommentId(1, 1, createOrUpdateComment);

        // Assertions
        assertNotNull(result);
        assertEquals("Updated comment", result.getText());
        verify(userService, times(1)).getUser();
        verify(commentRepository, times(1)).findAllCommentByAdIdAndAuthorIdAndIdComment(anyInt(), anyInt(), anyInt());
        verify(commentRepository, times(1)).save(any(CommentEntity.class));
        verify(commentMapper, times(1)).toDto(any(CommentEntity.class));
    }

}