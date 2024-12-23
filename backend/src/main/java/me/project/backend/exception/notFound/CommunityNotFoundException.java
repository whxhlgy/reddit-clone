package me.project.backend.exception.notFound;

import me.project.backend.exception.ServiceException;
import org.springframework.http.HttpStatus;

public class CommunityNotFoundException extends ServiceException {
    public CommunityNotFoundException(String name) {
        super("cannot find community: " + name);
    }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.NOT_FOUND;
  }
}
