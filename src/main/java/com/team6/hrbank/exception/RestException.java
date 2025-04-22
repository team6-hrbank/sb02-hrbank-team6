package com.team6.hrbank.exception;

import lombok.Getter;

@Getter
public class RestException extends RuntimeException {
  private final Code code;

  public RestException(Code code) {
    super(code.getMessage());
    this.code = code;
  }
}
