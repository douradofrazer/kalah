package com.fraz.demo.kalah.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid operation")
public class InvalidOperationException extends RuntimeException {

  public InvalidOperationException(String message) {
    super(message);
  }
}
