package me.project.backend.exception;

import org.springframework.http.HttpStatus;

public abstract class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }

    public abstract HttpStatus getHttpStatus();
}
