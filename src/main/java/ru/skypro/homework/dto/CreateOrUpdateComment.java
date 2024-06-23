package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class CreateOrUpdateComment {
    @Schema(required = true, description = "текст комментария")
    @Size(max = 64, min = 8)
    private String text;
}
