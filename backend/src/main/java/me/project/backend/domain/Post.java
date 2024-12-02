package me.project.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private Long communityId;

    private String title;

    private String content;

    private Long likesCount;
}
