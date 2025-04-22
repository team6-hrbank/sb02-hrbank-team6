package com.team6.hrbank.exception;

import java.util.NoSuchElementException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // 커스텀 예외 처리
  @ExceptionHandler(RestException.class)
  public ResponseEntity<ErrorResponse> handleRestException(RestException ex) {
    ErrorResponse errorResponse = new ErrorResponse(ex.getCode(), ex.getMessage());
    return ResponseEntity.status(ex.getCode().getStatus()).body(errorResponse);
  }

  // 404: Not Found 예외 처리
  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex) {
    ErrorResponse errorResponse = new ErrorResponse(ErrorCode.NOT_FOUND, ex.getMessage());
    return ResponseEntity.status(ErrorCode.NOT_FOUND.getStatus()).body(errorResponse);
  }

  // 400: Bad Request 예외 처리
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
    ErrorResponse errorResponse = new ErrorResponse(ErrorCode.BAD_REQUEST,
        ex.getMessage());
    return ResponseEntity.status(ErrorCode.BAD_REQUEST.getStatus()).body(errorResponse);
  }

  // 403: FORBIDDEN 예외 처리
  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
    ErrorResponse errorResponse = new ErrorResponse(ErrorCode.FORBIDDEN, ex.getMessage());
    return ResponseEntity.status(ErrorCode.FORBIDDEN.getStatus()).body(errorResponse);
  }

  // 500: 서버 내부 오류 처리
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex) {
    ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, ex.getMessage());
    return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus()).body(errorResponse);
  }

}
