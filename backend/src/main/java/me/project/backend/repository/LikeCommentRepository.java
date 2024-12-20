package me.project.backend.repository;

import me.project.backend.domain.Comment;
import me.project.backend.domain.LikeComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {

    Optional<LikeComment> findLikeCommentByUsernameAndComment(String username, Comment comment);

}
