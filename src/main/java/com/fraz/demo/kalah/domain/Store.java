package com.fraz.demo.kalah.domain;

import com.fraz.demo.kalah.exception.InvalidOperationException;

public class Store implements Pit {

  private String owner;
  private int seeds = 0;

  public Store() {}

  @Override
  public String getOwner() {
    return this.owner;
  }

  @Override
  public void setOwner(String owner) {
    this.owner = owner;
  }

  @Override
  public int getSeeds() {
    return this.seeds;
  }

  public void depositSeeds(String owner, int seeds) {
    if (null == owner || !owner.equals(this.owner)) {
      throw new InvalidOperationException("Invalid move");
    }
    this.seeds += seeds;
  }

  @Override
  public PitType getPitType() {
    return Pit.PitType.STORE;
  }
}
