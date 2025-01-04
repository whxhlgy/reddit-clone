package me.project.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import me.project.backend.domain.Feed;
import me.project.backend.domain.Post;
import org.springframework.data.repository.query.Param;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    Feed findByUserId(Long userId);

    @Query("SELECT p FROM Feed f JOIN f.posts p WHERE f.user.username = :username ORDER BY p.createdAt DESC")
    Page<Post> findPostsByUsername(@Param("username") String username, Pageable pageable);
}
