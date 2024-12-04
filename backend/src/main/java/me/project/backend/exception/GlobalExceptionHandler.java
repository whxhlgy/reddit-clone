package me.project.backend.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ServiceException.class)
    public ErrorResponse GlobalServiceExceptionHandler(ServiceException e) {
        return new ErrorResponse(e.getHttpStatus().value(), e.getMessage());
    }
}
