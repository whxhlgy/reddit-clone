package me.project.backend.exception.auth;

import me.project.backend.exception.ServiceException;
import me.project.backend.util.ErrorCode;
import org.springframework.http.HttpStatus;

public class BadJwtTokenException extends ServiceException {
    public BadJwtTokenException(String message) {
        super("Invalid refresh token: " + message);
    }

    @Override
    public String getErrorCode() {
        return ErrorCode.BAD_JWT_TOKEN;
    }

    @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.UNAUTHORIZED;
  }
}
