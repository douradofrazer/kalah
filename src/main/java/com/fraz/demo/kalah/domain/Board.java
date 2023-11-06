package com.fraz.demo.kalah.domain;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Board {
  private final Pit[] pits = new Pit[14];

  public Board() {

    pits[6] = new Store();
    pits[13] = new Store();

    for (int i = 0; i < pits.length; i++) {
      if (null == pits[i]) {
        pits[i] = new House();
      }
    }
  }

  public Board(Pit[] pits) {

    Arrays.stream(pits)
        .collect(Collectors.groupingBy(Pit::getPitType, Collectors.counting()))
        .forEach(
            (pitType, count) -> {
              if (pitType.equals(Pit.PitType.HOUSE) && (count != 12)) {
                throw new IllegalArgumentException();
              } else if (pitType.equals(Pit.PitType.STORE) && count != 2) {
                throw new IllegalArgumentException();
              }
            });

    for (int i = 0; i < pits.length; i++) {
      this.pits[i] = pits[i];
    }
  }

  public Pit getPit(int pitId) {
    return pits[pitId];
  }

  public Pit[] getPits() {
    return pits;
  }
}
