package me.project.backend.repository;

import me.project.backend.domain.CommentLike;
import me.project.backend.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findPostLikeByUsernameAndPostId(String username, Long postId);

    @Query("SELECT SUM(pl.reaction) FROM PostLike pl WHERE pl.post.id = :post_id")
    Optional<Integer> sumPostLikeByCommentId(@Param("post_id") Long postId);
}
