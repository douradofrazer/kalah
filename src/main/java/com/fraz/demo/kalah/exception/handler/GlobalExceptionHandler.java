package com.fraz.demo.kalah.exception.handler;

import com.fraz.demo.kalah.exception.ErrorTO;
import com.fraz.demo.kalah.exception.GameNotPlayableException;
import com.fraz.demo.kalah.exception.InvalidOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Collections;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({InvalidOperationException.class, GameNotPlayableException.class})
  public ErrorTO handleCustomException(InvalidOperationException ex) {
    ResponseStatus annotation = ex.getClass().getAnnotation(ResponseStatus.class);
    return new ErrorTO(
        annotation.code(), annotation.reason(), Collections.singletonList(ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ErrorTO handleException(Exception ex) {
    return new ErrorTO(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Unexpected error.",
        Collections.singletonList(ex.getMessage()));
  }
}
