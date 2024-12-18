package me.project.backend.exception.notFound;

public class CommentNotFoundException extends ResourceNotFoundException {
    public CommentNotFoundException(Long commentId) {
        super("Comment with id: " + commentId);
    }
}
