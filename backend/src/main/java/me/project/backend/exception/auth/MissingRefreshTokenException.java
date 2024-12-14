package me.project.backend.exception.auth;

import me.project.backend.exception.ServiceException;
import org.springframework.http.HttpStatus;

public class MissingRefreshTokenException extends ServiceException {
    public MissingRefreshTokenException(String message) {
        super(message);
    }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.BAD_REQUEST;
  }
}
