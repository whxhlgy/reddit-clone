package me.project.backend.exception.notFound;

public class PostNotFoundException extends ResourceNotFoundException {
    public PostNotFoundException(int postId) {
        super("Post with id " + postId);
    }
}
