package me.project.backend.exception.auth;

import me.project.backend.exception.ServiceException;
import org.springframework.http.HttpStatus;

import static me.project.backend.util.ErrorCode.BAD_CREDENTIALS;

public class InvalidCredentialsException extends ServiceException {

  public InvalidCredentialsException() {
    super("Username or password is not correct");
  }

  @Override
  public String getErrorCode() {
    return BAD_CREDENTIALS;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.UNAUTHORIZED;
  }
}
