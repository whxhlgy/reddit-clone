package me.project.backend.exception.auth;

import me.project.backend.exception.ServiceException;
import org.springframework.http.HttpStatus;

public class BadJwtTokenException extends ServiceException {
    public BadJwtTokenException(String message) {
        super("Invalid refresh token: " + message);
    }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.UNAUTHORIZED;
  }
}
