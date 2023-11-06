package com.fraz.demo.kalah.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StoreTest {
  // A Store
  // 1. Has zero seeds when created
  // 2. Can be deposited with seeds by its owner
  // 3. Cannot be deposited with seeds by the opponent
  // 4. Default pit type is STORE
  // 5. Can set an owner

  private final String player = "john_doe";
  private final int defaultSeedCount = 0;

  @Test
  public void testStoreHasZeroSeedsWhenCreated() {
    Store store = new Store();
    assertEquals(defaultSeedCount, store.getSeeds());
  }

  @Test
  public void testStoreCanBeDepositedWithSeedsByItsOwner() {
    Store store = new Store();
    store.setOwner(player);
    store.depositSeeds(player, 1);
    assertEquals(1, store.getSeeds());
  }

  @Test
  public void testStoreCannotBeDepositedWithSeedsByTheOpponent() {
    Store store = new Store();
    Assertions.assertThrows(
        RuntimeException.class,
        () -> {
          store.depositSeeds("jane_doe", 1);
        });
  }

  @Test
  public void testDefaultPitTypeIsStore() {
    Store store = new Store();
    assertEquals(Pit.PitType.STORE, store.getPitType());
  }
}
