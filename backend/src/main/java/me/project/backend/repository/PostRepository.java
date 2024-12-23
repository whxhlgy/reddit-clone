package me.project.backend.repository;

import jakarta.persistence.Entity;
import me.project.backend.domain.Post;
import me.project.backend.payload.dto.PostDTO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = { "user" })
    List<Post> findPostByCommunityName(String name);
}
