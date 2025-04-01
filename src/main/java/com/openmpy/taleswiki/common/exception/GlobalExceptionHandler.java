package com.openmpy.taleswiki.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customException(final CustomException e) {
        log.warn(e.getMessage(), e);
        return ResponseEntity.badRequest().body(ErrorResponse.of(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> authenticationException(final AuthenticationException e) {
        log.warn(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.of(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> httpRequestMethodNotSupportedException(
            final HttpRequestMethodNotSupportedException e
    ) {
        log.warn(e.getMessage(), e);
        return ResponseEntity.badRequest().body(ErrorResponse.of(CustomErrorCode.REQUEST_METHOD_NOT_SUPPORTED));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> noResourceException(final NoResourceFoundException e) {
        log.warn(e.getMessage(), e);
        return ResponseEntity.badRequest().body(ErrorResponse.of(CustomErrorCode.NO_RESOURCE_REQUEST));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> httpMessageNotReadableException(final HttpMessageNotReadableException e) {
        log.warn(e.getMessage(), e);
        return ResponseEntity.badRequest().body(ErrorResponse.of(CustomErrorCode.MESSAGE_NOT_READABLE));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(final Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.internalServerError().body(ErrorResponse.of(CustomErrorCode.INTERNAL_SERVER_ERROR));
    }
}
