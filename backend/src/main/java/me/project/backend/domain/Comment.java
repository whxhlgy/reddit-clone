package me.project.backend.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    private String username;

    private String content;

    private Long parentCommentId;

    public Comment(String username, String content) {
        this.username = username;
        this.content = content;
    }
}
