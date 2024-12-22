package me.project.backend.repository;

import me.project.backend.domain.Comment;
import me.project.backend.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findCommentLikeByUsernameAndComment(String username, Comment comment);

}
