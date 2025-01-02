package me.project.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import me.project.backend.domain.Feed;
import me.project.backend.domain.Post;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    @EntityGraph(attributePaths = { "posts" })
    List<Post> findAllByUserUsername(String username);

    Feed findByUserId(Long userId);
}
