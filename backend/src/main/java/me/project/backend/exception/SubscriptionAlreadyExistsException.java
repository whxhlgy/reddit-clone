package me.project.backend.exception;

import org.springframework.http.HttpStatus;

public class SubscriptionAlreadyExistsException extends ServiceException {
    public SubscriptionAlreadyExistsException(String message) {
        super(message);
    }

  @Override
  public String getErrorCode() {
    return "SUBSCRIPTION_ALREADY_EXISTS";
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.CONFLICT;
  }
}
