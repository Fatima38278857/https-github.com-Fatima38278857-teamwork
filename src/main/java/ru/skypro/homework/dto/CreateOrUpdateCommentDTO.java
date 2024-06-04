package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@Schema(name = "CreateOrUpdateComment")
public class CreateOrUpdateCommentDTO {
    @Size(min = 8, max = 64)
    @Schema(description = "Текст комментария")
    private String text;

}
