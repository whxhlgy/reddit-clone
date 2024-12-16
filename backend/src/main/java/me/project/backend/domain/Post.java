package me.project.backend.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString(exclude = "comments")
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private Long communityId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Long likesCount;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public void deleteComment(Comment comment) {
        comments.remove(comment);
        comment.setPost(null);
    }
}
