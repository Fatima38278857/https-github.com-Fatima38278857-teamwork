package ru.skypro.homework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class CommentEntity {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;
    @Column(name = "created_at")
    private Long createdAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private UserEntity author;
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "ad_id", nullable = false)
    private Ad ad;

    public CommentEntity(Long createdAt, UserEntity author, String text, Ad ad) {
        this.createdAt = createdAt;
        this.author = author;
        this.text = text;
        this.ad = ad;
    }
}
