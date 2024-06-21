package ru.skypro.homework.service.impl;

import org.springframework.security.core.Authentication;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private AdRepository adRepository;
    private CommentMapper commentMapper;
    private UserService userService;

    public CommentServiceImpl(CommentRepository commentRepository, AdRepository adRepository, CommentMapper commentMapper, UserService userService) {
        this.commentRepository = commentRepository;
        this.adRepository = adRepository;
        this.commentMapper = commentMapper;
        this.userService = userService;
    }

    @Override
    public List<CommentDTO> getAllComment(Integer adId) {
        List<CommentEntity> comment = commentRepository.findAll();
        return comment.stream().map(a -> commentMapper.toDto(a))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO createComment(Integer adId, CommentEntity comment, Authentication authentication) {
        UserDto user = userService.getUser();
        Optional<Ad> adById = adRepository.findById(adId);
        if (adById.isEmpty()) {
            throw new AdNotFoundException("ad not found");
        }else{
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
    public void deleteComment(Integer adId, Integer commentId) {
        commentRepository.deleteCommentByAd_IdAndId(adId,commentId);
    }

    @Override
    public CommentDTO patchCommentId(Integer adId, Integer commentId, CreateOrUpdateComment createOrUpdateComment, Authentication authentication) {

        UserDto userDto = userService.getUser();
        Integer userId = userDto.getId();
        List<CommentEntity> commentList = commentRepository.findAllCommentByAdIdAndAuthorIdAndIdComment(adId, userId, commentId);
        CommentEntity comment = commentList.get(0);
        if (comment == null) {
            throw new NotFoundCommentException("NOT_FOUND_EXCEPTION_DESCRIPTION");
        } else {
            comment.setText(createOrUpdateComment.getText());

            commentRepository.save(comment);
            return commentMapper.toDto(comment);
        }
    }
}
