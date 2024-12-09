package me.project.backend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<?> GlobalServiceExceptionHandler(ServiceException e) {
        log.debug("Globally handle service exception: {}, with exception: {}", e.getMessage(), e.getClass().getSimpleName());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        errorResponse.setStatus(e.getHttpStatus().value());
        errorResponse.setErrorCode(e.getErrorCode());
        return new ResponseEntity<>(errorResponse, e.getHttpStatus());
    }
}
