package me.project.backend.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    private String content;

    private Long parentCommentId;
}
