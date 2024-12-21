package me.project.backend.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class LikeComment {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;

    private int reaction;
}
