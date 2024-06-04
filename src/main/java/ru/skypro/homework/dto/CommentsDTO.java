package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "Comments")
public class CommentsDTO {
    @Schema(description = "общее количество комментариев")
    private int count;
    private List<CommentDTO> results;
}
