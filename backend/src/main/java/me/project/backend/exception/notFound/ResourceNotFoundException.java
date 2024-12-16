package me.project.backend.exception.notFound;

import me.project.backend.exception.ServiceException;
import org.springframework.http.HttpStatus;

public abstract class ResourceNotFoundException extends ServiceException {

    public ResourceNotFoundException(String resource) {
        super("Resource Not found: " + resource);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
