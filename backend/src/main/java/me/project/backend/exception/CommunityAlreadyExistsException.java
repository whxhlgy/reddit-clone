package me.project.backend.exception;

import org.springframework.http.HttpStatus;

public class CommunityAlreadyExistsException extends ServiceException {

    public CommunityAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }
}
