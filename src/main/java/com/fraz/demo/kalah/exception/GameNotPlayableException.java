package com.fraz.demo.kalah.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Game not playable")
public class GameNotPlayableException extends RuntimeException {

  public GameNotPlayableException(String message) {
    super(message);
  }
}
