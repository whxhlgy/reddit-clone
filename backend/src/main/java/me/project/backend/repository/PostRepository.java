package me.project.backend.repository;

import jakarta.persistence.Entity;
import me.project.backend.domain.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = { "comments" })
    Optional<Post> findPostWithCommentsById(long postId);
}
