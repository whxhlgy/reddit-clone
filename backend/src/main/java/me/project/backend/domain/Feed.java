package me.project.backend.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "feed_post")
    private List<Post> posts;

    public void addPost(Post post) {
        post.getFeeds().add(this);
        posts.add(post);
    }

    public void removePost(Post post) {
        post.getFeeds().remove(this);
        posts.add(post);
    }

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
