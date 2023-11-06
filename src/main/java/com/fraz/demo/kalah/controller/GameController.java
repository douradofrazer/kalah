package com.fraz.demo.kalah.controller;

import com.fraz.demo.kalah.service.GameService;
import com.fraz.demo.kalah.transferobj.GameDto;
import com.fraz.demo.kalah.transferobj.BoardStateDto;
import com.fraz.demo.kalah.transferobj.PlayerDto;
import com.fraz.demo.kalah.transferobj.GameMoveDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("v1/games")
@Validated
public class GameController {

  private final GameService gameService;

  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  @PostMapping("/init")
  @ResponseStatus(HttpStatus.CREATED)
  public GameDto initGame(@RequestBody @Valid PlayerDto player) {
    return gameService.init(player);
  }

  @GetMapping("/{gameRef}/status")
  @ResponseStatus(HttpStatus.OK)
  public BoardStateDto getBoardState(@PathVariable("gameRef") String gameRef) {
    return gameService.getBoardState(gameRef);
  }

  @PutMapping("/{gameRef}/join")
  @ResponseStatus(HttpStatus.OK)
  public GameDto joinGame(
      @PathVariable("gameRef") UUID gameRef, @RequestBody @Valid PlayerDto player) {
    return gameService.join(gameRef, player);
  }

  @PutMapping("/{gameRef}/move")
  @ResponseStatus(HttpStatus.OK)
  public BoardStateDto makeMove(
      @PathVariable("gameRef") UUID gameRef, @RequestBody @Valid GameMoveDto move) {
    return gameService.makeMove(gameRef, move);
  }
}
