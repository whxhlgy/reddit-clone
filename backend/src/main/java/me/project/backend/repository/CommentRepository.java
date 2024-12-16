package me.project.backend.repository;

import me.project.backend.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {

}
