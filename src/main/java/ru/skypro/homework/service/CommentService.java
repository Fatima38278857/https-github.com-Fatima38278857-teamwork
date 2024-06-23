package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getAllComment(Integer adId);

    CommentDTO createComment(Integer adId, CommentEntity comment);

    void deleteComment(Integer adId, Integer commentId);

    CommentDTO patchCommentId(Integer adId, Integer commentId, CreateOrUpdateComment createOrUpdateComment);
}
