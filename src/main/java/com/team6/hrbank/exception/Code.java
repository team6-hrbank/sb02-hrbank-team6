package com.team6.hrbank.exception;

import org.springframework.http.HttpStatus;

public interface Code {
  HttpStatus getStatus();
  String getMessage();
}
