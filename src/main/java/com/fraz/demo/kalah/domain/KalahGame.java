package com.fraz.demo.kalah.domain;

import com.fraz.demo.kalah.domain.constant.GameStatus;
import com.fraz.demo.kalah.service.mapper.Default;
import lombok.Getter;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class KalahGame {

  private final UUID gameReference;
  private final Board board;
  private final String playerOne;
  private String playerTwo;
  private String currentPlayer;
  private GameStatus gameStatus;
  private String winner;

  public KalahGame(String playerOne) {
    this.gameReference = UUID.randomUUID();
    this.board = new Board();
    this.playerOne = playerOne; // Whoever initializes a game is player one.
    this.currentPlayer = playerOne;
    IntStream.range(0, 7).forEach(i -> setStoreOwner(i, playerOne));
    gameStatus = GameStatus.INITIALIZED;
  }

  @Default
  public KalahGame(
      UUID gameReference,
      Board board,
      String playerOne,
      String playerTwo,
      String currentPlayer,
      GameStatus gameStatus,
      String winner) {
    this.gameReference = gameReference;
    this.board = board;
    this.playerOne = playerOne;
    this.playerTwo = playerTwo;
    this.currentPlayer = currentPlayer;
    this.gameStatus = gameStatus;
    this.winner = winner;
  }

  private void setStoreOwner(int pitId, String owner) {
    this.board.getPit(pitId).setOwner(owner);
  }

  public void joinGame(final String gameReference, final String playerTwo) {
    if (null == playerTwo || playerTwo.isEmpty()) {
      throw new RuntimeException("Player cannot be null/empty");
    } else if (null != this.playerTwo) {
      throw new RuntimeException("Game already has two players!");
    } else if (playerOne.equals(playerTwo)) {
      throw new RuntimeException("Player cannot play against self");
    }
    if (this.gameReference.toString().equals(gameReference)) {
      this.playerTwo = playerTwo;
      IntStream.range(7, 14).forEach(i -> setStoreOwner(i, playerTwo));
      this.gameStatus = GameStatus.IN_PROGRESS;
    } else {
      throw new RuntimeException("Invalid game reference");
    }
  }

  public void isGamePlayable() {
    // if missing players, not playable
    if (null == this.playerTwo || null == this.playerOne) {
      throw new RuntimeException("Game is not playable, not enough players");
    }

    // if finished, not playable
    if (GameStatus.FINISHED.equals(this.getGameStatus())) {
      throw new RuntimeException("Game has ended.");
    }
  }

  public void makeMove(final String player, int pitId) {

    Pit pit = this.board.getPit(pitId);

    if (null == pit
        || !this.currentPlayer.equals(player)
        || !player.equals(pit.getOwner())
        || pit.getPitType().equals(Pit.PitType.STORE)
        || pit.getSeeds() == 0) {
      throw new RuntimeException("Invalid move");
    }

    int seeds = pit.getSeeds();
    ((House) pit).emptyHouse();

    int moves = seeds;

    while (moves > 0) {

      pitId = (pitId + 1);

      if (pitId > 13) {
        pitId = 0;
      }

      Pit cursorPit = this.board.getPit(pitId);

      // skipping store that does not below to the player
      if (cursorPit.getPitType().equals(Pit.PitType.STORE)
          && !this.currentPlayer.equals(cursorPit.getOwner())) {
        continue;
      } else if (cursorPit.getPitType().equals(Pit.PitType.STORE)
          && this.currentPlayer.equals(cursorPit.getOwner())) {
        ((Store) cursorPit).depositSeeds(this.currentPlayer, 1);
        moves--;
      } else {
        ((House) cursorPit).sowSeeds(1);
        moves--;
      }
    }

    Pit cursorAtPit = this.board.getPit(pitId);

    captureSeeds(cursorAtPit, pitId);
    switchPlayer(pitId);
  }

  void captureSeeds(Pit cursorAtPit, int pitId) {

    if (cursorAtPit.getPitType().equals(Pit.PitType.HOUSE)
        && cursorAtPit.getOwner().equals(this.currentPlayer)
        && cursorAtPit.getSeeds() == 1) {

      Pit oppositePit = this.board.getPit(12 - pitId);

      if (oppositePit.getSeeds() > 0) {
        Arrays.stream(this.board.getPits())
            .filter(
                p ->
                    Pit.PitType.STORE.equals(p.getPitType())
                        && this.currentPlayer.equals(p.getOwner()))
            .forEach(
                s ->
                    ((Store) s)
                        .depositSeeds(
                            this.currentPlayer, oppositePit.getSeeds() + cursorAtPit.getSeeds()));
        ((House) cursorAtPit).emptyHouse();
        ((House) oppositePit).emptyHouse();
      }
    }
  }

  private void switchPlayer(int pitId) {

    Pit cursorAtPit = this.board.getPit(pitId);

    if (!Pit.PitType.STORE.equals(cursorAtPit.getPitType())) {
      this.currentPlayer = currentPlayer.equals(playerOne) ? playerTwo : playerOne;
    }
  }

  public GameStatus endGameCondition() {
    int playerOneSumOfSeeds = IntStream.range(0, 6).map(i -> this.board.getPit(i).getSeeds()).sum();
    int playerTwoSumOfSeeds =
        IntStream.range(7, 13).map(i -> this.board.getPit(i).getSeeds()).sum();

    if (playerOneSumOfSeeds == 0 || playerTwoSumOfSeeds == 0) {

      String playerWithNonEmptyPits = playerOneSumOfSeeds == 0 ? playerTwo : playerOne;

      Arrays.stream(this.board.getPits())
          .filter(
              p ->
                  Pit.PitType.STORE.equals(p.getPitType())
                      && playerWithNonEmptyPits.equals(p.getOwner()))
          .forEach(
              s ->
                  ((Store) s)
                      .depositSeeds(s.getOwner(), playerOneSumOfSeeds + playerTwoSumOfSeeds));

      Arrays.stream(this.board.getPits())
          .filter(p -> Pit.PitType.HOUSE.equals(p.getPitType()))
          .forEach(p -> ((House) p).emptyHouse());

      this.winner = declareWinner();

      return GameStatus.FINISHED;
    } else {
      return GameStatus.IN_PROGRESS;
    }
  }

  private String declareWinner() {
    return Arrays.stream(this.board.getPits())
        .filter(p -> Pit.PitType.STORE.equals(p.getPitType()))
        .collect(
            Collectors.collectingAndThen(
                Collectors.groupingBy(Pit::getOwner, Collectors.summingInt(Pit::getSeeds)),
                result -> {
                  if (result.get(playerOne) > result.get(playerTwo)) {
                    return playerOne;
                  } else if (result.get(playerOne) < result.get(playerTwo)) {
                    return playerTwo;
                  } else {
                    return "Draw";
                  }
                }));
  }
}
