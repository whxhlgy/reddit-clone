package me.project.backend.repository;

import me.project.backend.domain.Comment;
import me.project.backend.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostAndParentId(Post post, Long parentId);


    @Query(
            """
SELECT c
FROM Comment c
JOIN CommentClosure cc
ON cc.ancestor.id = :ancestor_id
WHERE c.id = cc.descendant.id
"""
    )
    List<Comment> findCommentByAncestorId(@Param("ancestor_id") Long id);

    @Query(
            """
SELECT c
FROM Comment c
LEFT JOIN CommentClosure cc
ON cc.ancestor IN (
    SELECT p
    FROM Comment p
    WHERE p.post.id = :post_id
    AND p.parentId IS NULL
)
WHERE c.id = cc.descendant.id
"""
    )
    List<Comment> findCommentsByPostId(@Param("post_id") Long id);
}
