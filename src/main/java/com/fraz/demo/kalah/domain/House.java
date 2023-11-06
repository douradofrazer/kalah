package com.fraz.demo.kalah.domain;

public class House implements Pit {

  private String owner;
  private int seeds = 6;

  public House() {}

  void emptyHouse() {
    this.seeds = 0;
  }

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

  public void sowSeeds(int seeds) {
    this.seeds += seeds;
  }
}
