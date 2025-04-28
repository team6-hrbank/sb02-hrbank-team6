package com.team6.hrbank.exception;

import java.time.Instant;

public record ErrorResponse(
    Instant timestamp,
    int status,
    String message,
    String details
) {
  public ErrorResponse(Code code, String details) {
    this(Instant.now(), code.getStatus().value(), code.getMessage(), details);
  }
}