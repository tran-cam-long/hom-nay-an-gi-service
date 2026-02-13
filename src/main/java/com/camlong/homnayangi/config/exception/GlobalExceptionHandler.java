package com.camlong.homnayangi.config.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

  public record ApiError(
      Instant timestamp,
      int status,
      String error,
      String message,
      String path
  ) {}

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiError handleValidation(MethodArgumentNotValidException ex,
                                   HttpServletRequest request) {

    String message = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(e -> e.getField() + ": " + e.getDefaultMessage())
        .collect(Collectors.joining(", "));

    return new ApiError(
        Instant.now(),
        HttpStatus.BAD_REQUEST.value(),
        "Validation Error",
        message,
        request.getRequestURI()
    );
  }

  @ExceptionHandler(BadCredentialsException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ApiError handleBadCredentials(BadCredentialsException ex,
                                       HttpServletRequest request) {

    return new ApiError(
        Instant.now(),
        401,
        "Unauthorized",
        "Invalid username or password",
        request.getRequestURI()
    );
  }

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ApiError handleNotFound(EntityNotFoundException ex,
                                 HttpServletRequest request) {

    return new ApiError(
        Instant.now(),
        404,
        "Not Found",
        ex.getMessage(),
        request.getRequestURI()
    );
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ApiError handleAccessDenied(AccessDeniedException ex,
                                     HttpServletRequest request) {

    return new ApiError(
        Instant.now(),
        403,
        "Forbidden",
        "You do not have permission to access this resource",
        request.getRequestURI()
    );
  }

  @ExceptionHandler(BusinessException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ApiError handleBusiness(BusinessException ex,
                                 HttpServletRequest request) {

    return new ApiError(
        Instant.now(),
        409,
        "Business Rule Violation",
        ex.getMessage(),
        request.getRequestURI()
    );
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiError handleAll(Exception ex,
                            HttpServletRequest request) {

    return new ApiError(
        Instant.now(),
        500,
        "Internal Server Error",
        "An unexpected error occurred",
        request.getRequestURI()
    );
  }

}
