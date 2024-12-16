package me.project.backend.exception.notFound;

public class PostNotFoundException extends ResourceNotFoundException {
    public PostNotFoundException(long postId) {
        super("Post with id " + postId);
    }
}
