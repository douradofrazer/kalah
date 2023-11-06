package com.fraz.demo.kalah.domain;

import com.fraz.demo.kalah.domain.constant.GameStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.stream.IntStream;

public class KalahGameTest {
  // Actions in the game
  // 1. Create a game
  // 2. Join a game
  // 3. Make a move
  // 4. Get game status
  // 5. Get game result

  private final String playerOne = "john_doe";
  private final String playerTwo = "jane_doe";

  @Test
  public void testGameInitialization() {
    KalahGame kalahGame = new KalahGame(playerOne);
    Assertions.assertAll(
        () -> Assertions.assertNotNull(kalahGame.getGameReference()),
        () -> Assertions.assertNotNull(kalahGame.getBoard()),
        () -> Assertions.assertEquals(14, kalahGame.getBoard().getPits().length),
        () -> Assertions.assertEquals(playerOne, kalahGame.getPlayerOne()),
        () -> Assertions.assertNull(kalahGame.getPlayerTwo()),
        () -> Assertions.assertEquals(playerOne, kalahGame.getCurrentPlayer()),
        () -> Assertions.assertEquals(GameStatus.INITIALIZED, kalahGame.getGameStatus()));
  }

  @Test
  public void testGameShouldNotBePlayableIfOnlyOnePlayerIsPresent() {
    // pre-conditions
    KalahGame kalahGame = new KalahGame(playerOne);
    Assertions.assertThrows(RuntimeException.class, kalahGame::isGamePlayable);
  }

  @Test
  public void testWhenPlayerTwoJoinsGameShouldBePlayable() {
    // pre-conditions
    KalahGame kalahGame = new KalahGame(playerOne);
    kalahGame.joinGame(kalahGame.getGameReference().toString(), playerTwo);

    Assertions.assertAll(
        () -> Assertions.assertNotNull(kalahGame.getGameReference()),
        () -> Assertions.assertEquals(playerOne, kalahGame.getPlayerOne()),
        () -> Assertions.assertEquals(playerTwo, kalahGame.getPlayerTwo()),
        () -> Assertions.assertEquals(playerOne, kalahGame.getCurrentPlayer()),
        () -> Assertions.assertEquals(GameStatus.IN_PROGRESS, kalahGame.getGameStatus()),
        () -> Assertions.assertDoesNotThrow(kalahGame::isGamePlayable));
  }

  @Test
  public void testWhenPlayerTwoJoinsGameThePitsShouldHaveCorrectOwners() {
    // pre-conditions
    KalahGame kalahGame = new KalahGame(playerOne);
    kalahGame.joinGame(kalahGame.getGameReference().toString(), playerTwo);

    IntStream.range(0, 14)
        .forEach(
            i -> {
              if (i < 7) {
                Assertions.assertEquals(playerOne, kalahGame.getBoard().getPit(i).getOwner());
              } else {
                Assertions.assertEquals(playerTwo, kalahGame.getBoard().getPit(i).getOwner());
              }
            });
  }

  @Test
  public void testGameShouldMarkGameAsFinishedWhenEitherPlayerSidePitsAreEmptyAndDeclareWinner() {
    // pre-conditions
    KalahGame kalahGame = new KalahGame(playerOne);
    kalahGame.joinGame(kalahGame.getGameReference().toString(), playerTwo);
    // creating a player's side with empty pits
    IntStream.range(0, 6).forEach(i -> ((House) kalahGame.getBoard().getPit(i)).emptyHouse());
    // setting final store values
    Store storePlayerOne = (Store) kalahGame.getBoard().getPit(6);
    storePlayerOne.depositSeeds(playerOne, 3);
    Store storePlayerTwo = (Store) kalahGame.getBoard().getPit(13);
    storePlayerTwo.depositSeeds(playerTwo, 10);

    GameStatus gameStatus = kalahGame.endGameCondition();

    Assertions.assertEquals(GameStatus.FINISHED, gameStatus);
    Assertions.assertEquals(playerTwo, kalahGame.getWinner());
  }

  @Test
  public void testGameShouldAllowPlayerToMakeAMoveForAValidPit() {
    // pre-conditions
    KalahGame kalahGame = new KalahGame(playerOne);
    kalahGame.joinGame(kalahGame.getGameReference().toString(), playerTwo);

    kalahGame.makeMove(playerOne, 0);

    Assertions.assertAll(
        () -> Assertions.assertEquals(0, kalahGame.getBoard().getPit(0).getSeeds()),
        () -> Assertions.assertEquals(7, kalahGame.getBoard().getPit(1).getSeeds()),
        () -> Assertions.assertEquals(7, kalahGame.getBoard().getPit(2).getSeeds()),
        () -> Assertions.assertEquals(7, kalahGame.getBoard().getPit(3).getSeeds()),
        () -> Assertions.assertEquals(7, kalahGame.getBoard().getPit(4).getSeeds()),
        () -> Assertions.assertEquals(7, kalahGame.getBoard().getPit(5).getSeeds()),
        () -> Assertions.assertEquals(1, kalahGame.getBoard().getPit(6).getSeeds()));
  }

  @Test
  public void testGameShouldAllowPlayerToContinuePlayingWhenLastSeedIsDroppedIntoHisOwnStore() {
    KalahGame kalahGame = new KalahGame(playerOne);
    kalahGame.joinGame(kalahGame.getGameReference().toString(), playerTwo);

    kalahGame.makeMove(playerOne, 0);
    Assertions.assertEquals(playerOne, kalahGame.getCurrentPlayer());
  }

  @Test
  public void testGameShouldSkipDepositIntoOpponentsStoreDuringMainPlayersMove() {
    KalahGame kalahGame = new KalahGame(playerOne);
    kalahGame.joinGame(kalahGame.getGameReference().toString(), playerTwo);

    kalahGame.makeMove(playerOne, 0);
    kalahGame.makeMove(playerOne, 5);
    kalahGame.makeMove(playerTwo, 7);
    kalahGame.makeMove(playerOne, 4);
    kalahGame.makeMove(playerTwo, 12);

    Assertions.assertAll(
        () -> Assertions.assertEquals(3, kalahGame.getBoard().getPit(6).getSeeds()),
        () -> Assertions.assertEquals(2, kalahGame.getBoard().getPit(13).getSeeds()));
  }

  @Test
  public void testGameShouldNotAllowPlayerToMakeAMoveForAnInvalidPit() {
    // pre-conditions
    KalahGame kalahGame = new KalahGame(playerOne);
    kalahGame.joinGame(kalahGame.getGameReference().toString(), playerTwo);

    Assertions.assertThrows(RuntimeException.class, () -> kalahGame.makeMove(playerOne, 7));
  }
}
