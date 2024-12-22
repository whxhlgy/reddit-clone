package me.project.backend.repository;

import me.project.backend.domain.Comment;
import me.project.backend.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findCommentLikeByUsernameAndCommentId(String username, Long comment_id);

    @Query("SELECT SUM(cl.reaction) FROM CommentLike cl WHERE cl.comment.id = :comment_id")
    Optional<Integer> sumCommentLikeByCommentId(@Param("comment_id") Long comment_id);

}
