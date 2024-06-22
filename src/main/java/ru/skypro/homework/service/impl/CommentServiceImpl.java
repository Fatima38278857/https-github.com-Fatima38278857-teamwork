package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.NotFoundCommentException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final CommentMapper commentMapper;
    private final UserService userService;

    public CommentServiceImpl(CommentRepository commentRepository, AdRepository adRepository, CommentMapper commentMapper, UserService userService) {
        this.commentRepository = commentRepository;
        this.adRepository = adRepository;
        this.commentMapper = commentMapper;
        this.userService = userService;
    }

    @Override
    public List<CommentDTO> getAllComment(Integer adId) {
        List<CommentEntity> comment = commentRepository.findAll();
        return comment.stream().map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO createComment(Integer adId, CommentEntity comment) {
        UserDto user = userService.getUser();
        Optional<Ad> adById = adRepository.findById(adId);
        if (adById.isEmpty()) {
            throw new AdNotFoundException("ad not found");
        } else {
            LocalDateTime localDateTime = LocalDateTime.now();

            ZonedDateTime zdt = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
            long date = zdt.toInstant().toEpochMilli();
            comment.setCreatedAt(date);
            comment.setAuthor(user.getId());
            comment.setAuthorFirstName(user.getFirstName());
            comment.setAd(adById.get());
            comment.setAuthorImage(user.getImage());

            return commentMapper.toDto(commentRepository.save(comment));
        }
    }

    @Override
    @Transactional
    public void deleteComment(Integer adId, Integer commentId) {
        commentRepository.deleteCommentByAdIdAndPk(adId, commentId);
    }

    @Override
    public CommentDTO patchCommentId(Integer adId, Integer commentId, CreateOrUpdateComment createOrUpdateComment) {
        UserDto userDto = userService.getUser();
        Integer authorId = userDto.getId();

        CommentEntity comment = commentRepository.findAllCommentByAdIdAndAuthorIdAndIdComment(adId, authorId, commentId);
        if (comment == null) {
            throw new NotFoundCommentException("NOT_FOUND_EXCEPTION_DESCRIPTION");
        } else {
            comment.setText(createOrUpdateComment.getText());

            commentRepository.save(comment);
            return commentMapper.toDto(comment);
        }
    }
}
