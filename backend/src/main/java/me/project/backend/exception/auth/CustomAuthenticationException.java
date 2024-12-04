package me.project.backend.exception.auth;

import me.project.backend.exception.ServiceException;
import org.springframework.http.HttpStatus;

public class CustomAuthenticationException extends ServiceException {
    public CustomAuthenticationException(String message) {
        super(message);
    }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.UNAUTHORIZED;
  }
}
