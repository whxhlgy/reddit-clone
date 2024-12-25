package me.project.backend.repository;

import me.project.backend.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = { "user" })
    Optional<Post> findById(long id);

    @EntityGraph(attributePaths = { "user" })
    Page<Post> findAllByCommunityName(Pageable pageable, String communityName);

    @EntityGraph(attributePaths = { "user" })
    @Query("SELECT p FROM Post p WHERE p.id IN :ids")
    List<Post> findAllInIds(@Param("ids") List<Long> ids);
}
