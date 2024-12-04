package me.project.backend.exception.auth;

import me.project.backend.exception.ServiceException;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends ServiceException {
  public InvalidCredentialsException() {
    super("The user is not authenticated because their credentials are invalid");
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.UNAUTHORIZED;
  }
}
