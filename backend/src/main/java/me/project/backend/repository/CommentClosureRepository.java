package me.project.backend.repository;

import me.project.backend.domain.Comment;
import me.project.backend.domain.CommentClosure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentClosureRepository extends JpaRepository<CommentClosure, Long> {

    List<CommentClosure> findAllByDescendant(Comment descendant);

    List<CommentClosure> findALlByAncestorAndDistanceGreaterThan(Comment ancestor, Integer distanceIsGreaterThan);
}
