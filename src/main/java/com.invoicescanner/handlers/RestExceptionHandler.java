package com.invoicescanner.handlers;

import com.invoicescanner.error.exception.ResourceNotFoundException;
import com.invoicescanner.model.wrapper.response.ApiResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class RestExceptionHandler {

  private static final Logger LOGGER = LogManager.getLogger(RestExceptionHandler.class);

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ApiResponseBody notFoundException(Exception exception) {
    if (LOGGER.isErrorEnabled()) {
      LOGGER.error("Not found error: " + exception.getMessage());
    }
    return new ApiResponseBody(exception.getMessage());
  }

  @ExceptionHandler(BadCredentialsException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ApiResponseBody badCredentialsException(Exception exception) {
    if (LOGGER.isErrorEnabled()) {
      LOGGER.error("Bad credentials error: " + exception.getMessage());
    }
    return new ApiResponseBody(exception.getMessage());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponseBody validationException(ConstraintViolationException exception) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Validation exception: ", exception);
    }
    ConstraintViolation<?> firstViolation = exception.getConstraintViolations().iterator().next();
    NodeImpl requestParam = ((PathImpl) firstViolation.getPropertyPath()).getLeafNode();

    String message = new StringBuilder("Request param ")
      .append(requestParam).append(" ").append(firstViolation.getMessage())
      .append(" but is ").append(firstViolation.getInvalidValue())
      .toString();
    return new ApiResponseBody(message);
  }
}
