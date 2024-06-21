package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private Integer pk;
    private Integer author;
    private Long createdAt;
    private String authorFirstName;
    private String authorImage;
    private String text;

    public CommentDTO(String text) {
        this.text = text;
    }
}
