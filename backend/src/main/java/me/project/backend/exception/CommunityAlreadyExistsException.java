package me.project.backend.exception;

public class CommunityAlreadyExistsException extends RuntimeException {
    public CommunityAlreadyExistsException() {
        super("The community already exists!");
    }
}
