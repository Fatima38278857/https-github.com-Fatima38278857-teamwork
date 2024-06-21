package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentsDTO;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.service.CommentService;

import java.util.List;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("ads")
public class CommentController {

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @Operation(summary = "получения комментариев")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "401"), @ApiResponse(responseCode = "403")})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("{id}/comments")
    public ResponseEntity<CommentsDTO> getCommentsByAdsId(@PathVariable Integer id, Authentication authentication) {
        log.info("getCommentsByAdsId in CommentController is used");
        CommentsDTO commentsDTO = new CommentsDTO();
        List<CommentDTO> allComment = commentService.getAllComment(id);
        commentsDTO.setCount(allComment.size() - 1);
        commentsDTO.setResults(allComment);

        return ResponseEntity.ok(commentsDTO);
    }

    @Operation(summary = "сознания комментария ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "401"), @ApiResponse(responseCode = "403")})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping(value = "{id}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Integer id, @RequestBody CreateOrUpdateComment createOrUpdateComment, Authentication authentication) {
        log.info("addComment in CommentController is used");
        CommentEntity comment = commentMapper.toComment(createOrUpdateComment);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(id, comment));
    }

    @Operation(summary = "удаление комментария ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "401"), @ApiResponse(responseCode = "403")})
    @PreAuthorize("hasRole('ADMIN') or @adServiceImpl.findById(id).author.email.equals(authentication.name)")
    @DeleteMapping("{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteCommentsByAdsId(@PathVariable Integer adId, @PathVariable Integer commentId, Authentication authentication) {
        commentService.deleteComment(adId, commentId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "исправление комментария ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "401"), @ApiResponse(responseCode = "403")})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping("{adId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Integer adId, @PathVariable Integer commentId, @RequestBody CreateOrUpdateComment comment, Authentication authentication) {
        log.info("updateComment in CommentController is used");
        return ResponseEntity.ok(commentService.patchCommentId(adId, commentId, comment));
    }

}