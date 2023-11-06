package com.fraz.demo.kalah.service;

import com.fraz.demo.kalah.domain.KalahGame;
import com.fraz.demo.kalah.domain.Pit;
import com.fraz.demo.kalah.domain.constant.GameStatus;
import com.fraz.demo.kalah.repository.GameInMemoryRepository;
import com.fraz.demo.kalah.repository.GameRepository;
import com.fraz.demo.kalah.repository.model.Game;
import com.fraz.demo.kalah.repository.model.Kalah;
import com.fraz.demo.kalah.service.mapper.KalahMapper;
import com.fraz.demo.kalah.transferobj.*;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Service
public class GameServiceImpl implements GameService {
  private final GameInMemoryRepository gameInMemoryRepository;

  private final GameRepository gameRepository;

  private final KalahMapper kalahMapper;

  public GameServiceImpl(
      GameInMemoryRepository gameInMemoryRepository,
      GameRepository gameRepository,
      KalahMapper kalahMapper) {
    this.gameInMemoryRepository = gameInMemoryRepository;
    this.gameRepository = gameRepository;
    this.kalahMapper = kalahMapper;
  }

  @Override
  public GameDto init(PlayerDto player) {
    KalahGame currentGame = new KalahGame(player.userName());
    Kalah savedGame = gameInMemoryRepository.save(kalahMapper.toKalah(currentGame));
    return new GameDto(
        savedGame.getGameReference(), savedGame.getPlayerOne(), savedGame.getPlayerTwo());
  }

  @Override
  public GameDto join(UUID gameRef, PlayerDto player) {
    Kalah game =
        gameInMemoryRepository
            .findById(gameRef)
            .orElseThrow(() -> new RuntimeException("Game not found"));

    KalahGame kalahGame = kalahMapper.toKalahGame(game);
    kalahGame.joinGame(gameRef.toString(), player.userName());

    Kalah updatedGame = gameInMemoryRepository.save(kalahMapper.toKalah(kalahGame));
    KalahGame updatedKalahGame = kalahMapper.toKalahGame(updatedGame);

    return new GameDto(
        updatedKalahGame.getGameReference(),
        updatedKalahGame.getPlayerOne(),
        updatedKalahGame.getPlayerTwo());
  }

  @Override
  public BoardStateDto getBoardState(String gameRef) {
    Kalah game =
        gameInMemoryRepository
            .findById(UUID.fromString(gameRef))
            .orElseThrow(() -> new RuntimeException("Game not found"));

    return new BoardStateDto(
        game.getGameReference().toString(),
        game.getGameStatus().toString(),
        game.getCurrentPlayer(),
        toPitsDto.apply(game.getBoard().getPits()),
        game.getWinner());
  }

  private final Function<Pit[], List<PitDto>> toPitsDto =
      (pitsArr) -> {
        List<PitDto> pits = new ArrayList<>();

        AtomicInteger houseIndex = new AtomicInteger(0);

        for (Pit pit : pitsArr) {
          pits.add(new PitDto(houseIndex.getAndIncrement(), pit.getOwner(), pit.getSeeds()));
        }

        return pits;
      };

  @Override
  public BoardStateDto makeMove(UUID gameRef, GameMoveDto move) {

    Kalah game =
        gameInMemoryRepository
            .findById(gameRef)
            .orElseThrow(() -> new RuntimeException("Game not found"));

    KalahGame kalahGame = kalahMapper.toKalahGame(game);
    kalahGame.isGamePlayable();
    kalahGame.makeMove(move.userName(), move.houseIndex());

    Kalah updatedGame = gameInMemoryRepository.save(kalahMapper.toKalah(kalahGame));

    if (GameStatus.FINISHED.equals(updatedGame.getGameStatus())) {
      saveGameToDB(updatedGame);
    }

    return new BoardStateDto(
        gameRef.toString(),
        updatedGame.getGameStatus().toString(),
        updatedGame.getCurrentPlayer(),
        toPitsDto.apply(updatedGame.getBoard().getPits()),
        updatedGame.getWinner());
  }

  private void saveGameToDB(Kalah kalah) {
    Game game = new Game();
    game.setReference(kalah.getGameReference());
    game.setSnapshot(kalah);
    gameRepository.save(game);
  }
}
