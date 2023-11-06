package com.fraz.demo.kalah.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BoardTest {
  // A Board
  // 1. Has 14 pits
  // 2. Has 12 houses and 2 stores

  @Test
  public void testBoardHas14Pits() {
    Board board = new Board();
    Assertions.assertEquals(14, board.getPits().length);
  }

  @Test
  public void testBoardHas12HousesAnd2Stores() {
    Board board = new Board();
    int houseCount = 0;
    int storeCount = 0;
    for (Pit pit : board.getPits()) {
      if (pit.getPitType() == Pit.PitType.HOUSE) {
        houseCount++;
      } else if (pit.getPitType() == Pit.PitType.STORE) {
        storeCount++;
      }
    }
    int finalHouseCount = houseCount;
    int finalStoreCount = storeCount;
    Assertions.assertAll(
        () -> Assertions.assertEquals(12, finalHouseCount),
        () -> Assertions.assertEquals(2, finalStoreCount));
  }

  @Test
  public void testBoardShouldHaveAStoreAfterEverySixHouses() {
    Board board = new Board();
    // kalaha board has 6 houses on each side and 14 pits in total
    Assertions.assertEquals(Pit.PitType.STORE, board.getPit(6).getPitType());
    Assertions.assertEquals(Pit.PitType.STORE, board.getPit(13).getPitType());
  }
}
