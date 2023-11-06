package com.fraz.demo.kalah.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HouseTest {
  // A house
  // 1. Has six seeds and an owner when created
  // 2. Can be emptied
  // 3. Can be sown with seeds
  // 5. Can be sown with seeds when it is empty
  // 6. Default pit type is HOUSE

  private final String player = "john_doe";
  private final int defaultSeedCount = 6;

  @Test
  public void testHouseHasSixSeedsAndOwnerWhenCreated() {
    House house = new House();
    house.setOwner(player);
    Assertions.assertAll(
        () -> assertEquals(defaultSeedCount, house.getSeeds()),
        () -> assertEquals(player, house.getOwner()));
  }

  @Test
  public void testHouseCanBeEmptied() {
    House house = new House();
    house.setOwner(player);
    house.emptyHouse();
    assertEquals(0, house.getSeeds());
  }

  @Test
  public void testHouseCanBeSownWithSeeds() {
    House house = new House();
    house.setOwner(player);
    house.sowSeeds(1);
    assertEquals(7, house.getSeeds());
  }

  @Test
  public void testHouseCanBeSownWithSeedsWhenItIsEmpty() {
    House house = new House();
    house.setOwner(player);
    house.emptyHouse();
    house.sowSeeds(2);
    assertEquals(2, house.getSeeds());
  }

  @Test
  public void testDefaultPitTypeIsHouse() {
    House house = new House();
    house.setOwner(player);
    assertEquals(Pit.PitType.HOUSE, house.getPitType());
  }
}
