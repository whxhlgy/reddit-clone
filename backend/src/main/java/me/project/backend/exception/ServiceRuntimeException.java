package me.project.backend.exception;

import org.springframework.http.HttpStatus;

public class ServiceRuntimeException extends ServiceException {

    public ServiceRuntimeException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
