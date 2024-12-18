package me.project.backend.repository;

import me.project.backend.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * @param commentId
     * @param generationNumber the depth of comment tree, the depth of root is 0
     * @return
     */
    @Query(
            nativeQuery = true,
            value = """
WITH RECURSIVE generation AS
(
SELECT comment.*, 0 AS generation_number
FROM comment
WHERE comment.id = ?1
UNION ALL
SELECT c.*, p.generation_number + 1 AS generation_number
FROM comment as c
INNER JOIN generation as p
ON p.id = c.parent_comment_id AND p.generation_number < ?2
)
SELECT c.id, c.parent_comment_id, c.post_id, c.content, c.username
FROM generation AS c
"""
    )
    List<Comment> findDescendant(long commentId, int generationNumber);
}
