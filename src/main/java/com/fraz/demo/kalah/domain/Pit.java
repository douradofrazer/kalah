package com.fraz.demo.kalah.domain;

public interface Pit {

  enum PitType {
    HOUSE,
    STORE
  }

  String getOwner();

  void setOwner(String owner);

  int getSeeds();

  default PitType getPitType() {
    return PitType.HOUSE;
  }
}
